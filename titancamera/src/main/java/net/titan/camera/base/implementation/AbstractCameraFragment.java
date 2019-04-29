package net.titan.camera.base.implementation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.chrisbanes.photoview.PhotoView;
import net.titan.camera.base.contracts.AbstractCameraFragmentView;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.fragment.util.listener.DoubleTapSafeOnClickListener;
import net.titan.camera.fragment.util.optional.Optional;
import net.titan.camera.interfaces.FragmentActivityCommunication;
import net.titan.camera.request.CameraRequestBuilder;
import net.titan.camera.request.ClientRequest;
import net.titan.camera.util.Constants;
import net.titan.camera.util.FileUtil;

import java.io.File;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public abstract class AbstractCameraFragment extends AbstractBaseFragment implements AbstractCameraFragmentView {

    protected View rootView;
    protected FrameLayout cameraContainer;
    protected View photoViewShow;
    protected View buttonCapture;
    protected View buttonDone;
    protected View buttonRetake;
    protected View viewOverlay;
    protected FrameLayout frameLayoutSwitchCamera;
    protected FrameLayout customLayoutContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Optional.doWhen(!isSafeObject(getClientRequest()), this::finishActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abstract_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        initViews(view);
        initOnClickListeners();
        initOnTouchListener();
        applyConfigurations();
    }

    @Nullable
    protected FragmentActivityCommunication getHostActivity() {
        return isInstanceOfActivity(FragmentActivityCommunication.class);
    }

    protected ClientRequest getClientRequest() {
        return Optional.ifPresent(getHostActivity(), FragmentActivityCommunication::getClientRequest);
    }


    @Nullable
    protected Bundle getClientParameters() {
        return getBaseFragmentActivity(fragmentActivity -> {
            return fragmentActivity.getIntent().getBundleExtra(Constants.CLIENT_PARAMETERS);
        });
    }

    @Override
    public boolean onBackPressed() {
        boolean shouldBack = photoViewShow.getVisibility() != View.VISIBLE;
        if (!shouldBack)
            onClickRetry();
        return shouldBack;
    }

    @Override
    public void displayImage(@NonNull Bitmap bitmap) {
        ((PhotoView) this.photoViewShow).setImageBitmap(bitmap);
    }

    @Override
    public void saveImageAndFinish(@NonNull Bitmap bitmap) {
        String rootPath = getClientRequest().getRootPath();
        File file;
        if (rootPath != null) {
            String imagePath = getClientRequest().getImageFileName();
            file = imagePath != null ? FileUtil.createImageFile(rootPath, imagePath) : FileUtil.createImageFile(rootPath);
        } else {
            file = FileUtil.createImageFile();
            if (file != null)
                makePicturePublic(file);
        }
        if (file != null && FileUtil.saveImageInFile(bitmap, file)) {
            sendSuccessResult(file);
        } else {
            showToast(R.string.image_save_error);
            sendFailureResultToClient();
            finishActivity();
        }
    }

    private void sendSuccessResult(@NonNull File requestedFile) {
        if (getClientRequest().isFromCallback())
            sendImageInCallback(requestedFile);
        else
            sendImageInOnActivityResult(requestedFile);
        finishActivity();
    }

    @Override
    public void setButtonCaptureVisibility(int visibility) {
        if (buttonCapture.getVisibility() != visibility)
            buttonCapture.post(() -> buttonCapture.setVisibility(visibility));
    }

    @Override
    public void setFrameLayoutSwitchCameraVisibility(int visibility) {
        if (frameLayoutSwitchCamera.getVisibility() != visibility && getClientRequest().isEnableSwitchCamera())
            frameLayoutSwitchCamera.post(() -> frameLayoutSwitchCamera.setVisibility(visibility));
    }

    @Override
    public void makePicturePublic(@NonNull File file) {
        getBaseFragmentActivity(fragmentActivity -> {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            fragmentActivity.sendBroadcast(mediaScanIntent);
        });
    }

    @CallSuper
    protected void initViews(@NonNull final View view) {
        buttonCapture = getButtonCapture();
        buttonDone = getButtonDone();
        buttonRetake = getButtonRetake();
        photoViewShow = getImageContainer();
        viewOverlay = view.findViewById(R.id.viewOverlay);
        cameraContainer = view.findViewById(R.id.cameraContainer);
        frameLayoutSwitchCamera = view.findViewById(R.id.frameLayoutSwitchCamera);
        customLayoutContainer = view.findViewById(R.id.customLayoutContainer);
    }

    @CallSuper
    protected void initOnClickListeners() {
        buttonCapture.setOnClickListener((DoubleTapSafeOnClickListener) view -> onClickCapture());
        buttonDone.setOnClickListener((DoubleTapSafeOnClickListener) view -> onClickDone());
        buttonRetake.setOnClickListener((DoubleTapSafeOnClickListener) view -> onClickRetry());
        frameLayoutSwitchCamera.setOnClickListener((DoubleTapSafeOnClickListener) view -> onClickSwitchCamera());
    }

    @SuppressLint("ClickableViewAccessibility")
    @CallSuper
    protected void initOnTouchListener() {
        cameraContainer.setOnTouchListener(this::onTouchToPreview);
    }

    protected View getImageContainer() {
        return rootView.findViewById(R.id.photoViewShow);
    }

    protected View getButtonCapture() {
        return rootView.findViewById(R.id.imageViewCapture);
    }

    protected View getButtonDone() {
        return rootView.findViewById(R.id.imageViewDone);
    }

    protected View getButtonRetake() {
        return rootView.findViewById(R.id.frameLayoutRetake);
    }

    protected boolean onTouchToPreview(View v, MotionEvent event) {
        return false;
    }

    private void sendImageInCallback(@NonNull File file) {
        getBaseFragmentContext(context -> {
            Intent intent = new Intent(Constants.BROADCAST_RECEIVER_CAPTURE_RESULTS);
            intent.putExtra(CameraRequestBuilder.RESULT_IMAGE_FILE, file);
            context.sendBroadcast(intent);
        });
    }

    private void sendImageInOnActivityResult(@NonNull File file) {
        getBaseFragmentActivity(fragmentActivity -> {
            Intent intent = new Intent();
            intent.putExtra(CameraRequestBuilder.RESULT_IMAGE_FILE, file);
            intent.putExtra(CameraRequestBuilder.PARAMETERS_BUNDLE, getClientParameters());
            fragmentActivity.setResult(Activity.RESULT_OK, intent);
        });
    }

    private void onClickCapture() {
        buttonCapture.setEnabled(false);
        viewOverlay.setVisibility(View.VISIBLE);
        photoViewShow.setVisibility(View.VISIBLE);
        onCapture();
        cameraContainer.setVisibility(View.GONE);
        buttonDone.setVisibility(View.VISIBLE);
        buttonRetake.setVisibility(View.VISIBLE);
        buttonCapture.setVisibility(View.GONE);
        viewOverlay.setVisibility(View.GONE);
        buttonCapture.setEnabled(true);
    }

    private void onClickDone() {
        buttonDone.setEnabled(false);
        onCompleted();
    }

    private void onClickRetry() {
        onRetry();
        cameraContainer.setVisibility(View.VISIBLE);
        buttonCapture.setVisibility(View.VISIBLE);
        photoViewShow.setVisibility(View.GONE);
        buttonDone.setVisibility(View.GONE);
        buttonRetake.setVisibility(View.GONE);
    }

    private void onClickSwitchCamera() {
        onSwitchCamera();
    }

    private void applyConfigurations() {
        getBaseFragmentContext(context -> {

            if (isSafeObject(getClientRequest())) {
                Integer customLayoutId = getClientRequest().getCustomLayoutId();

                if (customLayoutId != null)
                    LayoutInflater.from(context).inflate(customLayoutId, customLayoutContainer, true);
            }
        });
    }

    private void sendFailureResultToClient() {
        getBaseFragmentActivity(fragmentActivity -> {
            Intent intent = new Intent();
            intent.putExtra(Constants.IS_ERROR, true);
            intent.putExtra(Constants.ERROR_MESSAGE, Constants.FILE_CREATE_ERROR);
            if (getClientRequest().isFromCallback()) {
                intent.setAction(Constants.BROADCAST_RECEIVER_CAPTURE_RESULTS);
                fragmentActivity.sendBroadcast(intent);
            } else {
                intent.putExtra(CameraRequestBuilder.PARAMETERS_BUNDLE, getClientParameters());
                fragmentActivity.setResult(Activity.RESULT_CANCELED, intent);
            }
        });
    }

    protected abstract void onSwitchCamera();

    protected abstract void onCapture();

    protected abstract void onCompleted();

    protected abstract void onRetry();
}

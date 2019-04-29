package net.titan.camera.cameras.camera2api.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import net.titan.camera.base.implementation.AbstractCameraFragment;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.cameras.camera2api.contracts.Camera2FragmentPresenter;
import net.titan.camera.cameras.camera2api.contracts.Camera2FragmentView;
import net.titan.camera.cameras.camera2api.presenter.Camera2FragmentPresenterImpl;
import net.titan.camera.customviews.Camera2TextureView;
import net.titan.camera.interfaces.FragmentActivityCommunication;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2FragmentImpl extends AbstractCameraFragment implements Camera2FragmentView {

    private final Camera2FragmentPresenter presenter;
    private Camera2TextureView textureView;

    public Camera2FragmentImpl() {
        presenter = new Camera2FragmentPresenterImpl(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBaseFragmentActivity(fragmentActivity -> {
            presenter.setCameraManager((CameraManager) fragmentActivity.getSystemService(Context.CAMERA_SERVICE));
        });
        presenter.setCameraId(getClientRequest().getLensFacing());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume(textureView.isAvailable(), textureView.getWidth(), textureView.getHeight());
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void initViews(@NonNull View view) {
        super.initViews(view);
        getBaseFragmentContext(context -> {
            textureView = new Camera2TextureView(context);
            cameraContainer.addView(textureView);
        });
    }

    @Override
    protected void onSwitchCamera() {
        frameLayoutSwitchCamera.setVisibility(View.GONE);
        buttonCapture.setVisibility(View.GONE);
        presenter.onSwitchCamera();
    }

    @Override
    public void setTextureViewSurfaceTextureListener(@NonNull TextureView.SurfaceTextureListener surfaceTextureListener) {
        this.textureView.setSurfaceTextureListener(surfaceTextureListener);
    }

    @Override
    public void startCamera() {
        getBaseFragmentContext(context -> {
            cameraContainer.removeView(textureView);
            textureView = new Camera2TextureView(context);
            cameraContainer.addView(textureView);
            presenter.onResume(textureView.isAvailable(), textureView.getWidth(), textureView.getHeight());
        });
    }

    @Override
    protected boolean onTouchToPreview(View v, MotionEvent event) {
        return presenter.applyFocus();
    }

    @Override
    protected void onCapture() {
        try {
            presenter.onClickCapture(textureView.getBitmap());
        } catch (OutOfMemoryError error) {
            showToast(R.string.some_error_occurred);
            finishActivity();
        }
    }

    @Override
    protected void onCompleted() {
        presenter.onClickDone();
    }

    @Override
    protected void onRetry() {
    }

    @Override
    public void setTextureViewAspectRatio(int width, int height) {
        this.textureView.setAspectRatio(width, height);
    }

    @Override
    public Point getWindowSize() {
        Point point = new Point();
        getBaseFragmentActivity(fragmentActivity -> {
            fragmentActivity.getWindowManager().getDefaultDisplay().getSize(point);
        });
        return point;
    }

    @Override
    public int getWindowRotation() {
        Integer rotation = getBaseFragmentActivity(fragmentActivity -> {
            return fragmentActivity.getWindowManager().getDefaultDisplay().getRotation();
        });

        return rotation != null ? rotation : 90;
    }

    @Override
    public int getWindowOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    public void configureTransformTextureView(int viewWidth, int viewHeight, Size mPreviewSize) {
        getBaseFragmentActivity(fragmentActivity -> {
            if (null == textureView || null == mPreviewSize) {
                return;
            }
            int rotation = getWindowRotation();
            Matrix matrix = new Matrix();
            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
            RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
            float centerX = viewRect.centerX();
            float centerY = viewRect.centerY();
            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
                matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
                float scale = Math.max(
                        (float) viewHeight / mPreviewSize.getHeight(),
                        (float) viewWidth / mPreviewSize.getWidth());
                matrix.postScale(scale, scale, centerX, centerY);
                matrix.postRotate(90 * (rotation - 2), centerX, centerY);
            } else if (Surface.ROTATION_180 == rotation) {
                matrix.postRotate(180, centerX, centerY);
            }
            textureView.setTransform(matrix);
        });
    }

    @Override
    public SurfaceTexture getSurfaceTextureOfTextureView() {
        return textureView.getSurfaceTexture();
    }
}

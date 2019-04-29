package net.titan.camera.cameras.camera1api.views;

import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import net.titan.camera.base.implementation.AbstractCameraFragment;
import net.titan.camera.cameras.camera1api.callback.presenter.Camera1PreviewPresenter;
import net.titan.camera.cameras.camera1api.contracts.Camera1FragmentPresenter;
import net.titan.camera.cameras.camera1api.contracts.Camera1FragmentView;
import net.titan.camera.cameras.camera1api.presenter.Camera1FragmentPresenterImpl;
import net.titan.camera.customviews.Camera1Preview;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public class Camera1FragmentImpl extends AbstractCameraFragment implements Camera1FragmentView {

    private final Camera1FragmentPresenter presenter;

    public Camera1FragmentImpl() {
        presenter = new Camera1FragmentPresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected boolean onTouchToPreview(View v, MotionEvent event) {
        return presenter.applyFocus();
    }

    @Override
    public int getRequestedCameraId() {
        return getClientRequest().getLensFacing();
    }

    @Override
    public void setCamera1Preview(@NonNull Camera camera, int cameraId, @NonNull Camera1PreviewPresenter previewPresenter) {
        getBaseFragmentActivity(fragmentActivity -> {

            Camera1Preview camera1Preview = new Camera1Preview(fragmentActivity,
                    fragmentActivity,
                    cameraId,
                    camera,
                    previewPresenter);

            cameraContainer.removeAllViews();
            cameraContainer.addView(camera1Preview);
            presenter.setCamera1Preview(camera1Preview);
            setButtonCaptureVisibility(View.VISIBLE);
            setFrameLayoutSwitchCameraVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onCapture() {
        presenter.onClickCapture();
    }

    @Override
    protected void onCompleted() {
        presenter.onClickDone();
    }

    @Override
    protected void onRetry() {
    }

    @Override
    protected void onSwitchCamera() {
        presenter.onSwitchCamera();
    }
}

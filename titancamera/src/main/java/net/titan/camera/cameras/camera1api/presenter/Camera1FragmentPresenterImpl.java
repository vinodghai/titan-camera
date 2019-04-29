package net.titan.camera.cameras.camera1api.presenter;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.support.annotation.Nullable;

import net.titan.camera.cameras.CameraProperties;
import net.titan.camera.cameras.camera1api.callback.presenter.Camera1PreviewPresenter;
import net.titan.camera.cameras.camera1api.contracts.Camera1FragmentPresenter;
import net.titan.camera.cameras.camera1api.contracts.Camera1FragmentView;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.customviews.Camera1Preview;
import net.titan.camera.util.CameraUtil;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public class Camera1FragmentPresenterImpl implements Camera1FragmentPresenter, Camera1PreviewPresenter {

    private final Camera1FragmentView view;
    @Nullable
    private Camera1Preview camera1Preview;
    private Bitmap capturedBitmap;

    public Camera1FragmentPresenterImpl(Camera1FragmentView view) {
        this.view = view;
    }

    @Override
    public void setCamera1Preview(@Nullable Camera1Preview camera1Preview) {
        this.camera1Preview = camera1Preview;
    }

    @Override
    public void onResume() {
        if (camera1Preview != null)
            camera1Preview.startCamera();
        else
            this.getSupportedCamera(view.getRequestedCameraId());
    }

    @Override
    public void onPause() {
        if (camera1Preview != null)
            camera1Preview.releaseCamera();
    }

    @Override
    public void getSupportedCamera(int cameraId) {
        Camera camera;
        Object[] result = new Object[2];
        if (cameraId == CameraProperties.LENS_FACING_BACK)
            result = onBackCameraRequested();

        else if (cameraId == CameraProperties.LENS_FACING_FRONT)
            result = onFrontCameraRequested();

        camera = (Camera) result[0];
        if (camera != null)
            view.setCamera1Preview(camera, (int) result[1], this);
        else {
            view.showToast(R.string.camera_loading_error);
            view.finishActivity();
        }

    }

    private Object[] onFrontCameraRequested() {
        Camera camera = CameraUtil.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);

        if (camera == null) {
            view.showToast(R.string.front_camera_not_supported);
            return onBackCameraRequested();
        }
        return new Object[]{camera, Camera.CameraInfo.CAMERA_FACING_FRONT};
    }

    private Object[] onBackCameraRequested() {
        return new Object[]{CameraUtil.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK),
                Camera.CameraInfo.CAMERA_FACING_BACK};
    }

    @Override
    public boolean applyFocus() {
        if (camera1Preview != null) {
            camera1Preview.applyFocus();
            return true;
        }
        return false;
    }

    @Override
    public void finishActivity() {
        view.finishActivity();
    }

    @Override
    public void onClickCapture() {
        if (camera1Preview == null) {
            view.showToast(R.string.image_capture_error);
            view.finishActivity();
        } else {
            Bitmap bitmap = camera1Preview.getBitmap();
            if (bitmap != null) {
                view.displayImage(bitmap);
                this.capturedBitmap = bitmap;
            } else {
                view.showToast(R.string.image_capture_error);
            }
        }
    }

    @Override
    public void onClickDone() {
        if (capturedBitmap != null)
            view.saveImageAndFinish(capturedBitmap);
        else {
            view.showToast(R.string.image_capture_error);
            view.finishActivity();
        }
    }

    @Override
    public void onSwitchCamera() {
        this.onPause();
        if (camera1Preview != null) {
            this.getSupportedCamera(camera1Preview.getSwappedCameraId());
        } else
            view.finishActivity();
    }

    @Override
    public void showToast(int resId) {
        view.showToast(resId);
    }
}

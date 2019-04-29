package net.titan.camera.cameras.camera2api.callbacks.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import net.titan.camera.cameras.camera2api.callbacks.presenter.CaptureSessionStateCallbackPresenter;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2CaptureSessionStateCallback extends CameraCaptureSession.StateCallback {

    private final CaptureSessionStateCallbackPresenter captureSessionStateCallback;

    public Camera2CaptureSessionStateCallback(CaptureSessionStateCallbackPresenter captureSessionStateCallback) {
        this.captureSessionStateCallback = captureSessionStateCallback;
    }

    @Override
    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
        captureSessionStateCallback.createCameraCaptureSessionRequest(cameraCaptureSession);
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

    }
}

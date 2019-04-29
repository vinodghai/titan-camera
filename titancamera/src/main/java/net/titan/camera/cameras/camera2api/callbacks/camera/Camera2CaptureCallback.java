package net.titan.camera.cameras.camera2api.callbacks.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import net.titan.camera.cameras.camera2api.callbacks.presenter.CaptureCallbackPresenter;

/**
 * Created by VINOD KUMAR on 1/15/2019.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2CaptureCallback extends CameraCaptureSession.CaptureCallback {

    private final CaptureCallbackPresenter captureCallback;

    public Camera2CaptureCallback(CaptureCallbackPresenter captureCallback) {
        this.captureCallback = captureCallback;
    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);

    }

    @Override
    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
        super.onCaptureFailed(session, request, failure);
    }
}

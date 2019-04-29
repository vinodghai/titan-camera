package net.titan.camera.cameras.camera2api.callbacks.presenter;

import android.hardware.camera2.CameraCaptureSession;
import android.support.annotation.NonNull;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface CaptureSessionStateCallbackPresenter {

    void createCameraCaptureSessionRequest(@NonNull CameraCaptureSession cameraCaptureSession);
}

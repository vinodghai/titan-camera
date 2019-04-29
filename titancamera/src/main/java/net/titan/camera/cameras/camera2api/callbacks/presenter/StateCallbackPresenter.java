package net.titan.camera.cameras.camera2api.callbacks.presenter;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface StateCallbackPresenter {

    void showPreview(@NonNull CameraDevice cameraDevice);

    void closeCameraDevice();
}

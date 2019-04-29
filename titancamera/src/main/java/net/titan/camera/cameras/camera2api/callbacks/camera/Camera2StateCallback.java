package net.titan.camera.cameras.camera2api.callbacks.camera;

import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import net.titan.camera.cameras.camera2api.callbacks.presenter.StateCallbackPresenter;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2StateCallback extends CameraDevice.StateCallback {

    private final StateCallbackPresenter stateCallback;

    public Camera2StateCallback(StateCallbackPresenter stateCallback) {
        this.stateCallback = stateCallback;
    }

    @Override
    public void onOpened(@NonNull CameraDevice cameraDevice) {
        stateCallback.showPreview(cameraDevice);
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
        stateCallback.closeCameraDevice();

    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int i) {
        stateCallback.closeCameraDevice();

    }
}

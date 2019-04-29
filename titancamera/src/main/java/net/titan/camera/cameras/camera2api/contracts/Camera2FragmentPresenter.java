package net.titan.camera.cameras.camera2api.contracts;

import android.graphics.Bitmap;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public interface Camera2FragmentPresenter {

    void setCameraId(int requestedLensFacing);

    void setCameraManager(CameraManager cameraManager);

    void onResume(boolean isTextureViewAvailable, int width, int height);

    void onPause();

    boolean applyFocus();

    void onClickCapture(@NonNull Bitmap bitmap);

    void onClickDone();

    void onSwitchCamera();
}

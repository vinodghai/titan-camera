package net.titan.camera.cameras.camera1api.contracts;

import android.support.annotation.Nullable;

import net.titan.camera.customviews.Camera1Preview;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface Camera1FragmentPresenter {

    void setCamera1Preview(@Nullable Camera1Preview camera1Preview);

    void onResume();

    void onPause();

    void getSupportedCamera(int cameraId);

    boolean applyFocus();

    void onClickCapture();

    void onClickDone();

    void onSwitchCamera();
}

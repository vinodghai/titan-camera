package net.titan.camera.request;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import net.titan.camera.interfaces.Camera;

/**
 * Created by VINOD KUMAR on 1/9/2019.
 */
@SuppressWarnings("unused")
public class CameraRequestBuilder {

    public static final int REQUEST_CODE_CAMERA = 0;
    public static final String RESULT_IMAGE_FILE = "RESULT_IMAGE_FILE";
    public static final String PARAMETERS_BUNDLE = "PARAMETERS_BUNDLE";

    private Mediator mediator;

    public CameraRequestBuilder() {
        mediator = new Mediator();
    }

    public CameraRequestBuilder setLensFacing(int lensFacing) {
        mediator.getClientRequest().setLensFacing(lensFacing);
        return this;
    }

    public CameraRequestBuilder setEnableSwitchCamera(boolean enableSwitchCamera) {
        mediator.getClientRequest().setEnableSwitchCamera(enableSwitchCamera);
        return this;
    }

    public CameraRequestBuilder setCustomLayoutId(@Nullable @LayoutRes Integer layoutId) {
        mediator.getClientRequest().setCustomLayoutId(layoutId);
        return this;
    }

    public CameraRequestBuilder setRootPath(@Nullable String path) {
        mediator.getClientRequest().setRootPath(path);
        return this;
    }

    public CameraRequestBuilder setImageFileName(@Nullable String nameForImage) {
        mediator.getClientRequest().setImageFileName(nameForImage);
        return this;
    }

    public Camera build() {
        return mediator;
    }
}

package net.titan.camera.cameras.camera2api.callbacks.presenter;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface SurfaceTextureListenerPresenter {

    void setupCameraOutputs(int width, int height);

    void connectCamera(int width, int height);

    void configureTransform(int width, int height);

    void finish();
}

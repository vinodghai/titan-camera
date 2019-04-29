package net.titan.camera.cameras.camera2api.callbacks.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import net.titan.camera.cameras.camera2api.callbacks.presenter.SurfaceTextureListenerPresenter;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public class Camera2SurfaceTextureListener implements TextureView.SurfaceTextureListener {

    private final SurfaceTextureListenerPresenter surfaceTextureListener;

    public Camera2SurfaceTextureListener(SurfaceTextureListenerPresenter surfaceTextureListener) {
        this.surfaceTextureListener = surfaceTextureListener;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surfaceTextureListener.connectCamera(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        try {
            surfaceTextureListener.configureTransform(i, i1);
        } catch (Exception e) {
            surfaceTextureListener.finish();
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}

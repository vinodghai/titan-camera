package net.titan.camera.cameras.camera2api.contracts;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.util.Size;
import android.view.TextureView;

import net.titan.camera.base.contracts.AbstractCameraFragmentView;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public interface Camera2FragmentView extends AbstractCameraFragmentView {

    void setTextureViewSurfaceTextureListener(@NonNull TextureView.SurfaceTextureListener surfaceTextureListener);

    void startCamera();

    void setTextureViewAspectRatio(int width, int height);

    int getWindowRotation();

    int getWindowOrientation();

    void configureTransformTextureView(int viewWidth, int viewHeight, Size mPreviewSize);

    Point getWindowSize();

    SurfaceTexture getSurfaceTextureOfTextureView();
}

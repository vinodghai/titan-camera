package net.titan.camera.base.contracts;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface AbstractCameraFragmentView extends AbstractBaseFragmentView {

    void displayImage(@NonNull Bitmap bitmap);

    void setButtonCaptureVisibility(int visibility);

    void setFrameLayoutSwitchCameraVisibility(int visibility);

    void makePicturePublic(@NonNull File file);

    void saveImageAndFinish(@NonNull Bitmap bitmap);
}

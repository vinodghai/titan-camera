package net.titan.camera.interfaces;

import android.support.annotation.NonNull;

import net.titan.camera.exceptions.TitanCameraException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public interface CaptureResult {

    void onCapture(@NonNull File file);

    default void onFailure(@NonNull TitanCameraException t) {

    }
}

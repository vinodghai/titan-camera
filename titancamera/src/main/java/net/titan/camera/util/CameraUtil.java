package net.titan.camera.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */

public final class CameraUtil {

    private CameraUtil() {
    }

    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance(int lensFacing) {
        Camera c = null;
        try {
            c = Camera.open(lensFacing); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static int swapCamera1Id(int cameraId) {
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        return cameraId;
    }
}

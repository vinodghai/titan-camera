package net.titan.camera.interfaces;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
@SuppressWarnings("unused")
public interface Camera {

    void startCamera(@NonNull Context context, @NonNull CaptureResult captureResult);

    void startCamera(@NonNull Fragment fragment);

    void startCamera(@NonNull Fragment fragment, @Nullable Bundle parameters);

    void startCamera(@NonNull Activity activity);

    void startCamera(@NonNull Activity activity, @Nullable Bundle parameters);
}

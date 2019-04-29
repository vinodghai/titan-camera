package net.titan.camera.router;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public class FragmentRouter {

    private FragmentRouter() {
    }

    public static void add(@NonNull FragmentManager fragmentManager,
                           @IdRes Integer fragmentContainer,
                           @NonNull Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .add(fragmentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();
    }


    public static void replace(@NonNull FragmentManager fragmentManager,
                               @IdRes Integer fragmentContainer,
                               @NonNull Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(fragmentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}

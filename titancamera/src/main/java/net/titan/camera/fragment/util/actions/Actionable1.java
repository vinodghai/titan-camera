package net.titan.camera.fragment.util.actions;

import android.support.annotation.NonNull;

/**
 * Created by VINOD KUMAR on 10/3/2018.
 */
@FunctionalInterface
public interface Actionable1<T>{

    void action(@NonNull T t);
}

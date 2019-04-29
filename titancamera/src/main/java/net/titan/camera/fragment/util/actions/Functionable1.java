package net.titan.camera.fragment.util.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by VINOD KUMAR on 10/10/2018.
 */

@FunctionalInterface
public interface Functionable1<I, O> {

    @Nullable O apply(@NonNull I t);
}

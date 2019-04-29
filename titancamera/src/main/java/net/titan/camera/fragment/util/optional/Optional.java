package net.titan.camera.fragment.util.optional;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.titan.camera.fragment.util.actions.Actionable0;
import net.titan.camera.fragment.util.actions.Actionable1;
import net.titan.camera.fragment.util.actions.Functionable0;
import net.titan.camera.fragment.util.actions.Functionable1;

/**
 * Created by VINOD KUMAR on 4/16/2019.
 */
@SuppressWarnings("unused")
public final class Optional {

    public static void doWhen(boolean condition,
                              @NonNull Actionable0 actionable0Positive,
                              @NonNull Actionable0 actionable0Negative) {
        if (condition)
            actionable0Positive.action();
        else
            actionable0Negative.action();
    }


    public static void doWhen(boolean condition, @NonNull Actionable0 actionable0) {
        if (condition)
            actionable0.action();
    }


    @Nullable
    public static <O> O doWhen(boolean condition,
                               @NonNull Functionable0<O> functionable0) {
        return condition ? functionable0.apply() : null;
    }


    public static void ifPresent(@NonNull Actionable0 safeAction,
                                 @Nullable Object... objects) {
        boolean safe = true;

        if (objects != null) {
            for (Object object : objects)
                if (object == null)
                    safe = false;
        } else
            safe = false;


        if (safe)
            safeAction.action();
    }

    public static <T> void ifPresent(@Nullable T object, @NonNull Actionable1<T> actionable1) {
        if (object != null)
            actionable1.action(object);
    }


    @Nullable
    public static <T, O> O ifPresent(@Nullable T object, @NonNull Functionable1<T, O> actionable1) {
        return object != null ? actionable1.apply(object) : null;
    }


    public static <T> void ifPresent(@Nullable T object,
                                     @NonNull Actionable1<T> actionable11,
                                     @NonNull Actionable0 actionable2) {
        if (object != null)
            actionable11.action(object);
        else
            actionable2.action();
    }


    @Nullable
    public static <T, O> O ifPresent(@Nullable T object,
                                     @NonNull Functionable1<T, O> actionable1,
                                     @NonNull Actionable0 actionable2) {
        O o = null;
        if (object != null)
            o = actionable1.apply(object);
        else
            actionable2.action();
        return o;
    }


    @Nullable
    public static <T, O> O ifPresent(@Nullable T object,
                                     @NonNull Functionable1<T, O> functionable1,
                                     @NonNull Functionable0<O> functionable0) {

        return object != null ? functionable1.apply(object) : functionable0.apply();
    }


    @Nullable
    public static <O> O doWhen(boolean condition,
                               @NonNull Functionable0<O> functionable0,
                               @NonNull Functionable0<O> functionable01) {
        return condition ? functionable0.apply() : functionable01.apply();
    }


    public static <E, T> void isInstanceOf(@Nullable E object,
                                           @NonNull Class<T> clazz,
                                           @NonNull Actionable1<T> actionable,
                                           @NonNull Actionable0 actionable0) {

        if (clazz.isInstance(object)) {

            T castedObject = clazz.cast(object);

            if (castedObject != null)
                actionable.action(castedObject);
            else
                actionable0.action();


        } else
            actionable0.action();
    }

    public static <E, T> void isInstanceOf(@Nullable E object,
                                           @NonNull Class<T> clazz,
                                           @NonNull Actionable1<T> actionable) {

        if (clazz.isInstance(object)) {

            T castedObject = clazz.cast(object);

            if (castedObject != null)
                actionable.action(castedObject);
        }
    }
}

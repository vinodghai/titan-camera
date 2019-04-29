package net.titan.camera.fragment.util.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import net.titan.camera.fragment.util.actions.Actionable0;
import net.titan.camera.fragment.util.actions.Actionable1;
import net.titan.camera.fragment.util.actions.Functionable1;
import net.titan.camera.fragment.util.optional.Optional;

import java.util.List;


/**
 * Created by VINOD KUMAR on 4/16/2019.
 */

@SuppressWarnings("unused")
public class AbstractHelperFragment extends Fragment {

    /*
      FRAGMENT ACTIVITY CALL BACKS
   */
    protected final void getBaseFragmentActivity(@NonNull Actionable1<FragmentActivity> actionable1) {
        Optional.ifPresent(getActivity(), actionable1);
    }

    @Nullable
    protected final <O> O getBaseFragmentActivity(@NonNull Functionable1<FragmentActivity, O> actionable1) {
        return Optional.ifPresent(getActivity(), actionable1);
    }

    protected final void getBaseFragmentActivity(@NonNull Actionable1<FragmentActivity> actionable1,
                                                 @NonNull Actionable0 actionable2) {
        Optional.ifPresent(getActivity(), actionable1, actionable2);
    }

    @Nullable
    protected final <O> O getBaseFragmentActivity(@NonNull Functionable1<FragmentActivity, O> actionable1,
                                                  @NonNull Actionable0 actionable2) {
        return Optional.ifPresent(getActivity(), actionable1, actionable2);
    }

    /*
    FRAGMENT MANAGER CALL BACKS
     */

    protected final void getBaseFragmentManager(@NonNull Actionable1<FragmentManager> actionable1) {
        Optional.ifPresent(getFragmentManager(), actionable1);
    }

    @Nullable
    protected final <O> O getBaseFragmentManager(@NonNull Functionable1<FragmentManager, O> actionable1) {
        return Optional.ifPresent(getFragmentManager(), actionable1);
    }

    protected final void getBaseFragmentManager(@NonNull Actionable1<FragmentManager> actionable1,
                                                @NonNull Actionable0 actionable2) {
        Optional.ifPresent(getFragmentManager(), actionable1, actionable2);
    }

    @Nullable
    protected final <O> O getBaseFragmentManager(@NonNull Functionable1<FragmentManager, O> actionable1,
                                                 @NonNull Actionable0 actionable2) {
        return Optional.ifPresent(getFragmentManager(), actionable1, actionable2);
    }

    protected final void getBaseFragmentContext(@NonNull Actionable1<Context> actionable1) {
        Optional.ifPresent(getContext(), actionable1);
    }

    @Nullable
    protected final <O> O getBaseFragmentContext(@NonNull Functionable1<Context, O> actionable1) {
        return Optional.ifPresent(getContext(), actionable1);
    }

    protected final void getBaseFragmentContext(@NonNull Actionable1<Context> actionable1,
                                                @NonNull Actionable0 actionable12) {
        Optional.ifPresent(getContext(), actionable1, actionable12);
    }

    @Nullable
    protected final <O> O getBaseFragmentContext(@NonNull Functionable1<Context, O> actionable1,
                                                 @NonNull Actionable0 actionable2) {
        return Optional.ifPresent(getContext(), actionable1, actionable2);
    }

    protected final <T> void isInstanceOf(@NonNull Class<T> clazz,
                                          @NonNull Actionable1<T> actionable) {
        if (clazz.isInstance(this)) {
            T castObject = clazz.cast(this);
            if (castObject != null)
                actionable.action(castObject);
        }
    }


    protected final <T> void isInstanceOf(@NonNull Class<T> clazz,
                                          @NonNull Actionable1<T> actionable,
                                          @NonNull Actionable0 actionable0) {
        if (clazz.isInstance(this)) {
            T castObject = clazz.cast(this);
            if (castObject != null)
                actionable.action(castObject);
        } else {
            actionable0.action();
        }
    }

    protected final <T> void isInstanceOfActivity(@NonNull Class<T> clazz,
                                                  @NonNull Actionable1<T> actionable) {
        FragmentActivity fragmentActivity = getActivity();
        if (clazz.isInstance(fragmentActivity)) {
            T castActivity = clazz.cast(fragmentActivity);
            if (castActivity != null)
                actionable.action(castActivity);
        }
    }

    @Nullable
    protected final <T> T isInstanceOfActivity(@NonNull Class<T> clazz) {
        FragmentActivity fragmentActivity = getActivity();

        if (clazz.isInstance(fragmentActivity)) {

            T castActivity = clazz.cast(fragmentActivity);

            if (castActivity != null)
                return castActivity;
        }
        return null;
    }


    protected final <T> void isInstanceOfActivity(@NonNull Class<T> clazz,
                                                  @NonNull Actionable1<T> actionable,
                                                  @NonNull Actionable0 actionable0) {
        FragmentActivity fragmentActivity = getActivity();
        if (clazz.isInstance(getActivity())) {
            T castActivity = clazz.cast(fragmentActivity);
            if (castActivity != null)
                actionable.action(castActivity);
        } else {
            actionable0.action();
        }
    }

    protected boolean isSafeObject(@Nullable Object object) {
        return object != null;
    }

    protected boolean isEmptyList(List objectList) {
        return !isSafeObject(objectList) || objectList.isEmpty();
    }
}

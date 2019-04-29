package net.titan.camera.base.contracts;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface AbstractBaseFragmentView {

    void finishActivity();

    void showToast(@StringRes int resId);

    void showToast(@NonNull String s);
}

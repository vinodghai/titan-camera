package net.titan.camera.cameras.camera1api.callback.presenter;

import android.support.annotation.StringRes;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface Camera1PreviewPresenter {

    void finishActivity();

    void showToast(@StringRes int resId);
}

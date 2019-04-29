package net.titan.camera.fragment.util.listener;

import android.view.View;

import net.titan.camera.util.Constants;

/**
 * Created by VINOD KUMAR on 4/19/2019.
 */
public interface DoubleTapSafeOnClickListener extends View.OnClickListener {

    void doubleTapSafeOnClick(View view);


    @Override
    default void onClick(View v) {
        synchronized (Holder.LOCK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - Holder.clickTime > Constants.CLICK_THRESHOLD_MS) {
                Holder.clickTime = currentTime;
                doubleTapSafeOnClick(v);
            }
        }
    }

    class Holder {
        private static final Object LOCK = new Object();
        private static long clickTime = 0;
    }
}

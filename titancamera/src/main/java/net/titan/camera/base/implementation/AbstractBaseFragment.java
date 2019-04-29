package net.titan.camera.base.implementation;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import net.titan.camera.base.contracts.AbstractBaseFragmentView;
import net.titan.camera.fragment.util.base.AbstractHelperFragment;
import net.titan.camera.interfaces.FragmentActivityCommunication;


/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public abstract class AbstractBaseFragment extends AbstractHelperFragment implements AbstractBaseFragmentView {

    @Override
    public void onStart() {
        super.onStart();
        isInstanceOfActivity(FragmentActivityCommunication.class, f -> f.setCurrentFragment(this));
    }


    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void showToast(@StringRes int resId) {
        getBaseFragmentActivity(fragmentActivity -> {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(fragmentActivity, resId, Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void showToast(@NonNull String s) {
        getBaseFragmentActivity(fragmentActivity -> {
            fragmentActivity.runOnUiThread(() -> Toast.makeText(fragmentActivity, s, Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void finishActivity() {
        getBaseFragmentActivity(FragmentActivity::finish);
    }
}

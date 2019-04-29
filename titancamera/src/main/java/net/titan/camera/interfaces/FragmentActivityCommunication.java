package net.titan.camera.interfaces;

import android.support.annotation.NonNull;

import net.titan.camera.base.implementation.AbstractBaseFragment;
import net.titan.camera.request.ClientRequest;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public interface FragmentActivityCommunication {

    void setCurrentFragment(@NonNull AbstractBaseFragment currentFragment);

    ClientRequest getClientRequest();
}

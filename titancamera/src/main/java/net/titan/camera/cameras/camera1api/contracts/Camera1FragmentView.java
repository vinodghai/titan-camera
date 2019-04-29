package net.titan.camera.cameras.camera1api.contracts;

import android.hardware.Camera;
import android.support.annotation.NonNull;

import net.titan.camera.base.contracts.AbstractCameraFragmentView;
import net.titan.camera.cameras.camera1api.callback.presenter.Camera1PreviewPresenter;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public interface Camera1FragmentView extends AbstractCameraFragmentView {

    int getRequestedCameraId();

    void setCamera1Preview(@NonNull Camera camera, int cameraId, @NonNull Camera1PreviewPresenter previewPresenter);
}

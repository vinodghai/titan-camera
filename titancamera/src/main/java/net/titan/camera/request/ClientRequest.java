package net.titan.camera.request;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import net.titan.camera.cameras.CameraProperties;

import java.io.Serializable;

/**
 * Created by VINOD KUMAR on 1/9/2019.
 */
public class ClientRequest implements Serializable {

    private boolean enableSwitchCamera;

    private int lensFacing = CameraProperties.LENS_FACING_BACK;

    @Nullable
    @LayoutRes
    private Integer customLayoutId;

    private boolean isFromCallback = false;

    @Nullable
    private String rootPath;

    @Nullable
    private String imageFileName;

    @Nullable
    public String getImageFileName() {
        return imageFileName;
    }

    void setImageFileName(@Nullable String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Nullable
    public String getRootPath() {
        return rootPath;
    }

    void setRootPath(@Nullable String pathToSaveFile) {
        this.rootPath = pathToSaveFile;
    }

    public boolean isFromCallback() {
        return isFromCallback;
    }

    void setFromCallback(boolean fromCallback) {
        isFromCallback = fromCallback;
    }


    public boolean isEnableSwitchCamera() {
        return enableSwitchCamera;
    }

    void setEnableSwitchCamera(boolean enableSwitchCamera) {
        this.enableSwitchCamera = enableSwitchCamera;
    }


    public int getLensFacing() {
        return lensFacing;
    }

    void setLensFacing(int lensFacing) {
        this.lensFacing = lensFacing;
    }


    @Nullable
    @LayoutRes
    public Integer getCustomLayoutId() {
        return customLayoutId;
    }

    void setCustomLayoutId(@Nullable @LayoutRes Integer customView) {
        this.customLayoutId = customView;
    }
}

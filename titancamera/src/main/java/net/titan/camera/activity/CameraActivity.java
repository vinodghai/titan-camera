package net.titan.camera.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import net.titan.camera.base.implementation.AbstractBaseFragment;
import net.titan.camera.base.implementation.AbstractCameraFragment;
import net.titan.camera.cameras.camera1api.views.Camera1FragmentImpl;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.cameras.camera2api.views.Camera2FragmentImpl;
import net.titan.camera.interfaces.FragmentActivityCommunication;
import net.titan.camera.request.ClientRequest;
import net.titan.camera.router.FragmentRouter;
import net.titan.camera.util.CameraUtil;
import net.titan.camera.util.Constants;

public class CameraActivity extends AppCompatActivity implements FragmentActivityCommunication {

    private static final int REQUEST_CAMERA_ID = 1;
    private AbstractBaseFragment currentFragment;
    private ClientRequest clientRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (CameraUtil.checkCameraHardware(this)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            clientRequest = (ClientRequest) getIntent().getSerializableExtra(Constants.CLIENT_REQUEST);
            askPermissions();
        } else {
            sendFailureResultToClient();
            finish();
        }
    }

    @Override
    public void setCurrentFragment(@NonNull AbstractBaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == null || currentFragment.onBackPressed())
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_ID:
                if (grantResults.length > 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    execute();
                } else {
                    showToast(R.string.provide_permission_message);
                    finish();
                }
                break;
        }
    }

    private void sendFailureResultToClient() {
        Intent intent = new Intent();
        intent.putExtra(Constants.IS_ERROR, true);
        intent.putExtra(Constants.ERROR_MESSAGE, Constants.NO_CAMERA_EXISTS);
        if (clientRequest.isFromCallback()) {
            intent.setAction(Constants.BROADCAST_RECEIVER_CAPTURE_RESULTS);
            sendBroadcast(intent);
        } else
            setResult(Activity.RESULT_CANCELED, intent);
    }

    private void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    private void askPermissions() {
        if (isPermissionsGranted())
            execute();
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_CAMERA_ID);
    }

    private boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void execute() {
        AbstractCameraFragment cameraFragment;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            cameraFragment = new Camera2FragmentImpl();
        else
            cameraFragment = new Camera1FragmentImpl();

        FragmentRouter.add(getSupportFragmentManager(), R.id.fragmentContainer, cameraFragment);
    }
}

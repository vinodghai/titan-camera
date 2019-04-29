package com.example.vendvinodkumar.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.titan.camera.cameras.CameraProperties;
import net.titan.camera.interfaces.CaptureResult;
import net.titan.camera.request.CameraRequestBuilder;

import java.io.File;

public class MainActivity extends AppCompatActivity implements CaptureResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonStart).setOnClickListener(view -> new CameraRequestBuilder()
                .setEnableSwitchCamera(false)
                .setLensFacing(CameraProperties.LENS_FACING_BACK)
                .setCustomLayoutId(null)
                .build()
                .startCamera(this.getBaseContext(), this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == CameraRequestBuilder.REQUEST_CODE_CAMERA
                && data != null) {
            File file = (File) data.getSerializableExtra(CameraRequestBuilder.RESULT_IMAGE_FILE);
            if (file != null) {
                Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCapture(@NonNull File file) {
        Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();
    }
}

package net.titan.camera.request;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.titan.camera.activity.CameraActivity;
import net.titan.camera.exceptions.TitanCameraException;
import net.titan.camera.interfaces.Camera;
import net.titan.camera.interfaces.CaptureResult;
import net.titan.camera.util.Constants;

import java.io.File;

/**
 * Created by VINOD KUMAR on 1/10/2019.
 */
public class Mediator implements Camera {

    private ClientRequest clientRequest;

    Mediator() {
        this.clientRequest = new ClientRequest();
    }

    ClientRequest getClientRequest() {
        return clientRequest;
    }

    @Override
    public void startCamera(@NonNull Context context, @Nullable CaptureResult captureResult) {
        clientRequest.setFromCallback(true);
        registerReceiverToReceiveCaptureResults(context, captureResult);
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(Constants.CLIENT_REQUEST, clientRequest);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void startCamera(@NonNull Fragment fragment) {
        clientRequest.setFromCallback(false);
        Context context = fragment.getContext();
        if (context != null) {
            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra(Constants.CLIENT_REQUEST, clientRequest);
            fragment.startActivityForResult(intent, CameraRequestBuilder.REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void startCamera(@NonNull Fragment fragment, @Nullable Bundle parameters) {
        clientRequest.setFromCallback(false);
        Context context = fragment.getContext();
        if (context != null) {
            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra(Constants.CLIENT_REQUEST, clientRequest);
            intent.putExtra(Constants.CLIENT_PARAMETERS, parameters);
            fragment.startActivityForResult(intent, CameraRequestBuilder.REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void startCamera(@NonNull Activity activity) {
        clientRequest.setFromCallback(false);
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(Constants.CLIENT_REQUEST, clientRequest);
        activity.startActivityForResult(intent, CameraRequestBuilder.REQUEST_CODE_CAMERA);
    }

    @Override
    public void startCamera(@NonNull Activity activity, @Nullable Bundle parameters) {
        clientRequest.setFromCallback(false);
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(Constants.CLIENT_REQUEST, clientRequest);
        intent.putExtra(Constants.CLIENT_PARAMETERS, parameters);
        activity.startActivityForResult(intent, CameraRequestBuilder.REQUEST_CODE_CAMERA);
    }

    private void registerReceiverToReceiveCaptureResults(@NonNull final Context context,
                                                         @Nullable final CaptureResult captureResult) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                if (captureResult != null) {
                    if (intent.getBooleanExtra(Constants.IS_ERROR, false))
                        captureResult.onFailure(new TitanCameraException(intent.getStringExtra(Constants.ERROR_MESSAGE)));
                    else
                        captureResult.onCapture((File) intent.getSerializableExtra(CameraRequestBuilder.RESULT_IMAGE_FILE));
                }
            }
        };
        context.registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_RECEIVER_CAPTURE_RESULTS));
    }
}

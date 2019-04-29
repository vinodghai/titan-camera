package net.titan.camera.cameras.camera2api.presenter;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.View;

import net.titan.camera.cameras.CameraProperties;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.cameras.camera2api.callbacks.camera.Camera2CaptureSessionStateCallback;
import net.titan.camera.cameras.camera2api.callbacks.camera.Camera2StateCallback;
import net.titan.camera.cameras.camera2api.callbacks.camera.Camera2SurfaceTextureListener;
import net.titan.camera.cameras.camera2api.callbacks.presenter.CaptureCallbackPresenter;
import net.titan.camera.cameras.camera2api.callbacks.presenter.CaptureSessionStateCallbackPresenter;
import net.titan.camera.cameras.camera2api.callbacks.presenter.StateCallbackPresenter;
import net.titan.camera.cameras.camera2api.callbacks.presenter.SurfaceTextureListenerPresenter;
import net.titan.camera.cameras.camera2api.contracts.Camera2FragmentPresenter;
import net.titan.camera.cameras.camera2api.contracts.Camera2FragmentView;
import net.titan.camera.util.Constants;
import net.titan.camera.util.ImageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2FragmentPresenterImpl implements Camera2FragmentPresenter,
        StateCallbackPresenter,
        SurfaceTextureListenerPresenter,
        CaptureSessionStateCallbackPresenter,
        CaptureCallbackPresenter {

    private final Camera2FragmentView view;
    private CameraManager cameraManager;
    private CameraCaptureSession cameraCaptureSession;
    private String cameraId;
    private int currentLensFacing;
    private CameraDevice cameraDevice;
    private HandlerThread backgroundThread;
    private Handler backgroundThreadHandler;
    private Size previewSize;
    private Bitmap capturedBitmap;
    private Surface previewSurface;

    public Camera2FragmentPresenterImpl(Camera2FragmentView view) {
        this.view = view;
    }

    @Override
    public void setCameraId(int requestedLensFacing) {
        try {

            this.currentLensFacing = requestedLensFacing;

            if (requestedLensFacing == CameraProperties.LENS_FACING_BACK)
                this.cameraId = onBackCameraRequested();

            else if (requestedLensFacing == CameraProperties.LENS_FACING_FRONT)
                this.cameraId = onFrontCameraRequested();

            if (cameraId == null) {
                view.showToast(R.string.camera_loading_error);
                view.finishActivity();
            }

        } catch (CameraAccessException e) {
            view.finishActivity();
        }
    }

    @Override
    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onResume(boolean isTextureViewAvailable, int width, int height) {
        try {
            startBackgroundThread();
            if (isTextureViewAvailable)
                connectCamera(width, height);
            else
                view.setTextureViewSurfaceTextureListener(new Camera2SurfaceTextureListener(this));
        } catch (Exception e) {
            view.showToast(R.string.camera_loading_error);
            this.finish();
        }
    }

    @Override
    public void onPause() {
        this.stopCamera();
    }

    @Override
    public boolean applyFocus() {
        if (cameraDevice != null && cameraCaptureSession != null) {
            try {
                CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
                this.setFpsRange(captureRequestBuilder);
                captureRequestBuilder.addTarget(previewSurface);
                cameraCaptureSession.capture(captureRequestBuilder.build(), null, backgroundThreadHandler);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void setupCameraOutputs(int width, int height) {
        CameraCharacteristics cameraCharacteristics;
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            Point windowSize = view.getWindowSize();
            int displayRotation = view.getWindowRotation();

            Integer mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            if (mSensorOrientation != null) {
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                }
            }

            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = windowSize.x;
            int maxPreviewHeight = windowSize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = windowSize.y;
                maxPreviewHeight = windowSize.x;
            }

            maxPreviewWidth = maxPreviewWidth > 1920 ? 1920 : maxPreviewWidth;
            maxPreviewHeight = maxPreviewHeight > 1080 ? 1080 : maxPreviewHeight;

            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            if (map != null) {
                Size fullScreenSize = ImageUtil.chooseOptimalSize(map.getOutputSizes(ImageFormat.JPEG), width, height);
                previewSize = ImageUtil.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth,
                        rotatedPreviewHeight,
                        maxPreviewWidth,
                        maxPreviewHeight,
                        fullScreenSize);
            } else
                previewSize = new Size(width, height);

            if (view.getWindowOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                this.view.setTextureViewAspectRatio(previewSize.getWidth(), previewSize.getHeight());
            } else {
                this.view.setTextureViewAspectRatio(previewSize.getHeight(), previewSize.getWidth());
            }


        } catch (CameraAccessException e) {
            view.showToast(R.string.camera_loading_error);
            view.finishActivity();
        }
    }

    @Override
    public void configureTransform(int width, int height) {
        this.view.configureTransformTextureView(width, height, previewSize);
    }

    @Override
    public void connectCamera(int width, int height) {
        try {
            setupCameraOutputs(width, height);
            configureTransform(width, height);
            openCamera();
        } catch (Exception e) {
            view.showToast(R.string.camera_loading_error);
            this.finish();
        }
    }


    @Override
    public void showPreview(@NonNull CameraDevice cameraDevice) {
        this.cameraDevice = cameraDevice;
        try {
            SurfaceTexture surfaceTexture = view.getSurfaceTextureOfTextureView();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            previewSurface = new Surface(surfaceTexture);
            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new Camera2CaptureSessionStateCallback(this),
                    backgroundThreadHandler);
        } catch (Exception e) {
            view.finishActivity();
        }
    }

    @Override
    public void createCameraCaptureSessionRequest(@NonNull CameraCaptureSession cameraCaptureSession) {
        Camera2FragmentPresenterImpl.this.cameraCaptureSession = cameraCaptureSession;
        try {
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            this.setFpsRange(captureRequestBuilder);
            this.setFirstAvailableAutoFocus(captureRequestBuilder);
            captureRequestBuilder.addTarget(previewSurface);
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                            view.setButtonCaptureVisibility(View.VISIBLE);
                            view.setFrameLayoutSwitchCameraVisibility(View.VISIBLE);
                        }
                    },
                    backgroundThreadHandler);
        } catch (Exception e) {
            view.showToast(R.string.some_error_occurred);
            view.finishActivity();
        }
    }

    @Override
    public void onClickCapture(@NonNull Bitmap bitmap) {
        this.capturedBitmap = bitmap;
        view.displayImage(bitmap);
    }

    @Override
    public void onClickDone() {
        if (capturedBitmap != null)
            view.saveImageAndFinish(capturedBitmap);
        else {
            view.showToast(R.string.image_capture_error);
            view.finishActivity();
        }
    }

    @Override
    public void closeCameraDevice() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    public void onSwitchCamera() {
        try {
            this.stopCamera();

            this.setCameraId(this.getSwappedLensFacing());

            if (cameraId != null) {
                view.startCamera();
            }
        } catch (Exception e) {
            view.showToast(R.string.switch_camera_error);
        }
    }

    @Override
    public void finish() {
        view.finishActivity();
    }

    private String onFrontCameraRequested() throws CameraAccessException {
        String cameraId = findCameraId(CameraCharacteristics.LENS_FACING_FRONT);
        if (cameraId == null) {
            try {
                view.showToast(R.string.front_camera_not_supported);
                cameraId = onBackCameraRequested();
                this.currentLensFacing = CameraProperties.LENS_FACING_BACK;
            } catch (Exception e) {
                //Left
            }
        }
        return cameraId;
    }

    private String onBackCameraRequested() throws CameraAccessException {
        return findCameraId(CameraCharacteristics.LENS_FACING_BACK);
    }

    private int getSwappedLensFacing() {
        if (currentLensFacing == CameraProperties.LENS_FACING_BACK)
            return CameraProperties.LENS_FACING_FRONT;

        else if (currentLensFacing == CameraProperties.LENS_FACING_FRONT)
            return CameraProperties.LENS_FACING_BACK;

        return currentLensFacing;
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {
            cameraManager.openCamera(cameraId, new Camera2StateCallback(this), backgroundThreadHandler);
        } catch (CameraAccessException e) {
            view.showToast(R.string.camera_in_use);
            view.finishActivity();
        }
    }

    private String findCameraId(int requestedLensFacing) throws CameraAccessException {
        for (String cameraID : cameraManager.getCameraIdList()) {
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
            Integer lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (lensFacing != null && lensFacing == requestedLensFacing) {
                return cameraID;
            }
        }
        return null;
    }

    @Nullable
    private Range<Integer> getFpsRange() {
        try {
            CameraCharacteristics characteristics;

            characteristics = cameraManager.getCameraCharacteristics(cameraId);

            Range<Integer>[] ranges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);

            Range<Integer> result = null;
            if (ranges != null)
                for (Range<Integer> range : ranges) {
                    if (result == null || range.getUpper() > result.getUpper())
                        result = range;
                }

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    private void setFpsRange(CaptureRequest.Builder captureRequestBuilder) {
        Range<Integer> fpsRange = getFpsRange();
        if (fpsRange != null)
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsRange);
    }

    private void setFirstAvailableAutoFocus(@NonNull CaptureRequest.Builder captureRequestBuilder) {
        List<Integer> autoFocusList = new ArrayList<>();
        autoFocusList.add(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        autoFocusList.add(CaptureRequest.CONTROL_AF_MODE_AUTO);
        autoFocusList.add(CaptureRequest.CONTROL_AF_MODE_MACRO);
        autoFocusList.add(CaptureRequest.CONTROL_AE_MODE_OFF);

        try {
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            int[] availableAutoFocusModes = cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            if (availableAutoFocusModes != null)
                for (Integer requiredAutoFocusType : autoFocusList)
                    for (int availableAutoFocusMode : availableAutoFocusModes)
                        if (requiredAutoFocusType == availableAutoFocusMode) {
                            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, requiredAutoFocusType);
                            return;
                        }


        } catch (Exception ignore) {
            //Ignore if auto-focus is not available
        }
    }

    private void stopCamera() {
        try {
            closeCamera();
            stopBackgroundThread();
        } catch (Exception ignore) {
            //Ignore
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread(Constants.CAMERA2_BACKGROUND_THREAD);
        backgroundThread.start();
        backgroundThreadHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundThreadHandler = null;
            } catch (Exception e) {
                //ignore
            }
        }
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
    }
}

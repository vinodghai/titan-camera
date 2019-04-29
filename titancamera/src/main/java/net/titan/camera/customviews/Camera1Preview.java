package net.titan.camera.customviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.titan.camera.cameras.camera1api.callback.presenter.Camera1PreviewPresenter;
import net.titan.camera.cameras.camera2api.R;
import net.titan.camera.util.CameraUtil;
import net.titan.camera.util.Constants;
import net.titan.camera.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by VINOD KUMAR on 1/8/2019.
 */
public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    private Activity activity;
    private Camera1PreviewPresenter previewPresenter;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private byte[] capturedBytes;
    private int cameraId;

    public Camera1Preview(Context context) {
        super(context);
    }

    public Camera1Preview(Context context, Activity activity, int cameraId, Camera camera, Camera1PreviewPresenter previewPresenter) {
        super(context);
        this.activity = activity;
        this.cameraId = cameraId;
        this.camera = camera;
        this.previewPresenter = previewPresenter;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.startPreview();
        } catch (Exception e) {
            previewPresenter.showToast(R.string.camera_loading_error);
            this.finisActivity();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        if (surfaceHolder.getSurface() != null) {
            // stop preview before making changes
            try {
                camera.stopPreview();
            } catch (Exception ignore) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here
            Camera.Parameters parameters = camera.getParameters();
            setPreviewSize(parameters, width, height);
            setSupportedAutoFocus(parameters);
            int orientation = Constants.PORTRAIT_ORIENTATION;
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
                orientation = getCorrectCameraOrientationForFace();
            camera.setDisplayOrientation(orientation);
            camera.setParameters(parameters);
            camera.setPreviewCallback(this);


            // start preview with new settings
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();

            } catch (Exception e) {
                previewPresenter.showToast(R.string.camera_loading_error);
                this.finisActivity();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            try {
                camera.stopPreview();
            } catch (Exception ignore) {
                // ignore: tried to stop a non-existent preview
            }
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        this.capturedBytes = bytes;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width > height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    public void applyFocus() {
        try {
            camera.autoFocus(null);
        } catch (Exception ignore) {
            //Cannot perform auto-focus - ignore
        }
    }

    @Nullable
    public Bitmap getBitmap() {
        try {
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;
            int previewFormat = camera.getParameters().getPreviewFormat();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            YuvImage yuvImage = new YuvImage(capturedBytes, previewFormat, width, height, null);
            yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, out);
            byte[] imageBytes = out.toByteArray();
            Bitmap bitmap = ImageUtil.decodeByteArray(imageBytes, width, height);
            boolean shouldFlip = cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT;
            Bitmap rotatedBitmap = ImageUtil.rotateBitmapTo90Degree(bitmap, shouldFlip);
            return rotatedBitmap != null ? rotatedBitmap : bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void setAspectRatio(int width, int height) {
        if (width >= 0 && height >= 0) {
            mRatioWidth = width;
            mRatioHeight = height;
            requestLayout();
        }
    }

    public void releaseCamera() {
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }
            if (surfaceHolder != null) {
                surfaceHolder.removeCallback(this);
                surfaceHolder = null;
            }
        } catch (Exception e) {
            this.finisActivity();
        }

    }

    public void startCamera() {
        try {
            this.camera = CameraUtil.getCameraInstance(cameraId);
            int orientation = Constants.PORTRAIT_ORIENTATION;
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
                orientation = getCorrectCameraOrientationForFace();
            camera.setDisplayOrientation(orientation);
            this.surfaceHolder = getHolder();
            this.surfaceHolder.addCallback(this);
            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.setPreviewCallback(this);
            this.camera.startPreview();
        } catch (Exception e) {
            previewPresenter.showToast(R.string.camera_loading_error);
            this.finisActivity();
        }
    }

    public int getSwappedCameraId() {
        return CameraUtil.swapCamera1Id(this.cameraId);
    }

    private void finisActivity() {
        this.releaseCamera();
        previewPresenter.finishActivity();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setPreviewSize(@NonNull Camera.Parameters parameters, int width, int height) {
        Camera.Size fullScreenSize = ImageUtil.chooseOptimalSize(parameters.getSupportedPictureSizes(), width, height);
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        Camera.Size bestSize = ImageUtil.chooseOptimalSize(parameters.getSupportedPreviewSizes(), width, height, point.x, point.y, fullScreenSize);
        parameters.setPreviewSize(bestSize.width, bestSize.height);
        setAspectRatio(bestSize.height, bestSize.width);
    }

    private void setSupportedAutoFocus(@NonNull Camera.Parameters parameters) {
        List<String> supportedFocusList = parameters.getSupportedFocusModes();

        if (supportedFocusList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        else if (supportedFocusList.contains(Camera.Parameters.FOCUS_MODE_AUTO))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
    }

    private int getCorrectCameraOrientationForFace() {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        return (360 - info.orientation) % 360;
    }
}

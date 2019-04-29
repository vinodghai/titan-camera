package net.titan.camera.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public final class ImageUtil {

    private ImageUtil() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Size chooseOptimalSize(Size[] choices,
                                         int textureViewWidth,
                                         int textureViewHeight,
                                         int maxWidth,
                                         int maxHeight,
                                         Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, (lhs, rhs) -> Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight()));
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, (lhs, rhs) -> Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight()));
        } else {
            return choices[0];
        }
    }

    @NonNull
    public static Camera.Size chooseOptimalSize(List<Camera.Size> choices,
                                                int textureViewWidth,
                                                int textureViewHeight,
                                                int maxWidth,
                                                int maxHeight,
                                                Camera.Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Camera.Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Camera.Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.width;
        int h = aspectRatio.height;
        for (Camera.Size option : choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                    option.height == option.width * h / w) {
                if (option.width >= textureViewWidth &&
                        option.height >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, (lhs, rhs) -> Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height));
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, (lhs, rhs) -> Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height));
        } else {
            return choices.get(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Size chooseOptimalSize(@NonNull Size[] outputSizes, int width, int height) {
        if (outputSizes.length > 0) {
            double preferredRatio = height / (double) width;
            Size currentOptimalSize = outputSizes[0];
            double currentOptimalRatio = currentOptimalSize.getWidth() / (double) currentOptimalSize.getHeight();
            for (Size currentSize : outputSizes) {
                double currentRatio = currentSize.getWidth() / (double) currentSize.getHeight();
                if (Math.abs(preferredRatio - currentRatio) <
                        Math.abs(preferredRatio - currentOptimalRatio)) {
                    currentOptimalSize = currentSize;
                    currentOptimalRatio = currentRatio;
                }
            }
            return currentOptimalSize;
        }
        return new Size(width, height);
    }

    @NonNull
    public static Camera.Size chooseOptimalSize(@NonNull List<Camera.Size> outputSizes, int width, int height) {
        double preferredRatio = height / (double) width;
        Camera.Size currentOptimalSize = outputSizes.get(0);
        double currentOptimalRatio = currentOptimalSize.width / (double) currentOptimalSize.height;
        for (Camera.Size currentSize : outputSizes) {
            double currentRatio = currentSize.width / (double) currentSize.height;
            if (Math.abs(preferredRatio - currentRatio) <
                    Math.abs(preferredRatio - currentOptimalRatio)) {
                currentOptimalSize = currentSize;
                currentOptimalRatio = currentRatio;
            }
        }
        return currentOptimalSize;
    }

    @Nullable
    public static Bitmap rotateBitmapTo90Degree(@NonNull Bitmap originalBitmap, boolean shouldFlip) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            if (shouldFlip)
                matrix.preScale(-1.0f, 1.0f);
            return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap decodeByteArray(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

package net.titan.camera.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
public final class FileUtil {

    private FileUtil() {
    }

    @Nullable
    public static File createImageFile() {
        File storageDir = getDefaultPublicPictureStorage();
        return storageDir != null ? createImageFile(storageDir.getAbsolutePath()) : null;
    }

    @Nullable
    public static File createImageFile(@NonNull String path) {
        return createImageFile(path, getDefaultImageFileName());
    }

    @Nullable
    public static File createImageFile(@NonNull String path, @NonNull String imageFileName) {
        File root = new File(path);
        return (root.exists() || root.mkdirs()) ? new File(path + imageFileName) : null;
    }

    public static boolean saveImageInFile(@NonNull Bitmap bitmap, @NonNull File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return true;
        } catch (IOException e) {
            return false;
        } catch (OutOfMemoryError error) {
            return false;
        }
    }

    private static String getDefaultImageFileName() {
        String timeStamp = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT, Locale.US).format(new Date());
        return "JPEG_" + timeStamp + "_" + Constants.JPG_EXTENSION;
    }

    @Nullable
    private static File getDefaultPublicPictureStorage() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }
}

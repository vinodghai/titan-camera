package net.titan.camera.util;

/**
 * Created by VINOD KUMAR on 12/26/2018.
 */
@SuppressWarnings("WeakerAccess")
public final class Constants {

    /*
        Strings
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyyMMdd_HHmmss";
    public static final String JPG_EXTENSION = ".jpg";
    public static final String CAMERA2_BACKGROUND_THREAD = "camera2BackgroundThread";
    public static final String NO_CAMERA_EXISTS = "No camera found!";
    public static final String FILE_CREATE_ERROR = "Cannot create file, File object is null";
    public static final String BROADCAST_RECEIVER_CAPTURE_RESULTS = "BROADCAST_RECEIVER_CAPTURE_RESULTS";
    public static final String IS_ERROR = "IS_ERROR";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String CLIENT_REQUEST = "CLIENT_REQUEST";
    public static final String CLIENT_PARAMETERS = "CLIENT_PARAMETERS";

    /*
        Integers
     */
    public static final Integer PORTRAIT_ORIENTATION = 90;


    /*
        Longs
     */
    public static final long CLICK_THRESHOLD_MS = 700;


    private Constants() {
    }
}

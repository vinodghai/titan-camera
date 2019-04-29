package net.titan.camera.fragment.util.optional;

import android.support.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by FARHAN DHANANI on 5/4/2018.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class TextUtil {

    public static boolean isEmpty(CharSequence text) {
        return !(text != null && text.toString().trim().length() > 0);
    }

    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    public static boolean isValidEmail(String emailAddress) {
        return TextUtil.isNotEmpty(emailAddress) && isValidWithRegex(emailAddress, Pattern.compile("^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE));
    }

    public static boolean isValidURL(String url) {
        return TextUtil.isNotEmpty(url) && isValidWithRegex(url, Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$", Pattern.CASE_INSENSITIVE));
    }

    public static boolean isValidWithRegex(CharSequence text, String regex) {
        return isValidWithRegex(text, Pattern.compile(regex));
    }

    public static boolean isValidWithRegex(CharSequence text, Pattern pattern) {
        return pattern.matcher(text).find();
    }

    public static boolean equals(@Nullable CharSequence a, @Nullable CharSequence b) {
        return equals(a, b, false);
    }

    public static boolean equals(@Nullable CharSequence a, @Nullable CharSequence b, boolean ignoreCase) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (ignoreCase) {
            a = ((String) a).toLowerCase();
            b = ((String) b).toLowerCase();
        }

        if (a == b) return true;
        int length = a.length();
        if (length == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public interface OnDelayListener {
        void onCompleted(CharSequence text);
    }
}
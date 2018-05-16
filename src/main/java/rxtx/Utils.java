package rxtx;

import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkArgument;

public class Utils {
    public static boolean isAbsolutePath(String filePath) {
        checkArgument(! Strings.isNullOrEmpty(filePath), "File path need to be provided!");

        if (filePath.startsWith("/") || filePath.indexOf(":") > 0) {
            return true;
        }

        return false;
    }

    public static String trans2AbsolutePath(String filePath) {
        String finalPath = filePath;

        if (! isAbsolutePath(filePath)) {
            finalPath = StandardSystemProperty.USER_DIR.value() + StandardSystemProperty.FILE_SEPARATOR.value() + filePath;
        }

        return finalPath;
    }

    public static void sleepInSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

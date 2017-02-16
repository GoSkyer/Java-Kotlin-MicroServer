package microserver.bio;

/**
 * Created by galaxy on 2017/2/9.
 */
public class SLog {

    private static boolean isDebug = true;

    private static String TAG = "TAG";

    public static void error(String s) {
        if (isDebug) {
            System.err.println(TAG + " - " + s);
        }
    }


}

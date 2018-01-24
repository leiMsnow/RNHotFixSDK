package ray.rn.hotfix.constants;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileConstant {

    public static final String JS_BUNDLE_NAME = "index.android.bundle";

    public static final String ZIP_NAME = "js.bundle.zip";

    public static final String TEMP_FOLDER = "/temp/";

    private static final String DRAWABLE_FOLDER = "/drawable-mdpi/";

    private static String APP_ROOT = Environment.getExternalStorageDirectory().toString();

    public static final String TEMP_ROOT_PATH = APP_ROOT + TEMP_FOLDER;

    public static final String TEMP_ZIP_PATH = TEMP_ROOT_PATH + ZIP_NAME;
    public static final String TEMP_JS_BUNDLE = TEMP_ROOT_PATH + JS_BUNDLE_NAME;
    public static final String TEMP_DRAWABLE = TEMP_ROOT_PATH + DRAWABLE_FOLDER;

    public static final String LOCAL_JS_BUNDLE = APP_ROOT + File.separator + JS_BUNDLE_NAME;
    public static final String LOCAL_DRAWABLE = APP_ROOT + DRAWABLE_FOLDER;

    public static final String JS_BUNDLE_REMOTE_URL = "http://172.12.11.95:10001/" + ZIP_NAME;
//    public static final String JS_BUNDLE_REMOTE_URL = "http://10.0.0.2:10001/" + ZIP_NAME;

    public static String getAppRoot(Context context){
       return APP_ROOT = Environment.getExternalStorageDirectory().toString()
                + File.separator + context.getPackageName();
    }
}

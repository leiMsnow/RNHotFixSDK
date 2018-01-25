package ray.rn.hotfix;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

import ray.rn.hotfix.constants.FileConstant;
import ray.rn.hotfix.fix.FixRequest;

/**
 * Created by wecash on 2018/1/4.
 */

public class HotFix {
    public static void run(Activity app) {
        FixRequest.request(app);
    }

    @Nullable
    public static String getJSBundleFile(Context context) {
        String path = FileConstant.getAppRoot(context) + FileConstant.JS_BUNDLE_NAME;
        File file = new File(path);
        if (file.exists()) {
            Log.d("getBundleFile", "'local_js_bundle'");
            return path;
        } else {
            return null;
        }
    }
}

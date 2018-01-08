package ray.rn.hotfix;

import android.app.Activity;

import ray.rn.hotfix.fix.FixRequest;

/**
 * Created by wecash on 2018/1/4.
 */

public class HotFix {
    public static void run(Activity app) {
        FixRequest.request(app);
    }
}

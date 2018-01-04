package com.wecash.hotfix;

import android.app.Activity;

import com.wecash.hotfix.fix.FixRequest;

/**
 * Created by wecash on 2018/1/4.
 */

public class HotFix {
    public static void init(Activity app) {
        FixRequest.request(app);
    }
}

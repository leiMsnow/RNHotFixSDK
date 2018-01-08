package com.ray.rn.hotfix.command;

import android.app.Activity;
import android.util.Log;

import com.ray.rn.hotfix.models.ResponseModels;

/**
 * Created by wecash on 2018/1/4.
 */

public class HotCommand implements ICommand {

    @Override
    public void notifyCommand(final Activity activity, final ResponseModels result) {
        Log.i(this.getClass().getName(), "notify");
    }

    @Override
    public void forceCommand(final Activity activity, ResponseModels result) {
        Log.i(this.getClass().getName(), "force");

    }

}

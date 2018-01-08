package ray.rn.hotfix.command;

import android.app.Activity;

import ray.rn.hotfix.models.ResponseModels;

/**
 * Created by wecash on 2018/1/4.
 */

public interface ICommand {

    void notifyCommand(final Activity context, ResponseModels result);

    void forceCommand(final Activity context, ResponseModels result);

}

package ray.rn.hotfix.fix;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import ray.rn.hotfix.command.ColdCommand;
import ray.rn.hotfix.command.HotCommand;
import ray.rn.hotfix.command.ICommand;
import ray.rn.hotfix.models.ResponseModels;

/**
 * Created by wecash on 2018/1/4.
 */

public class FixResponse {

    private MyHandler myHandler = null;

    public FixResponse(Activity mContext) {
        myHandler = new MyHandler(mContext, mContext.getMainLooper());
    }

    public void coldCommand(ResponseModels result) {
        Message message = myHandler.obtainMessage();
        message.what = 1;
        message.obj = result;
        myHandler.sendMessage(message);
    }

    public void hotCommand(ResponseModels result) {
        Message message = myHandler.obtainMessage();
        message.what = 2;
        message.obj = result;
        myHandler.sendMessage(message);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Activity> weakReference;

        public MyHandler(Activity context, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity context = weakReference.get();
            if (context != null) {
                final ResponseModels result = (ResponseModels) msg.obj;
                ICommand command = null;

                if (msg.what == 1) {
                    command = new ColdCommand();
                } else if (msg.what == 2) {
                    command = new HotCommand();
                }

                if (null != command) {
                    if ("notify".equals(result.remoteCommand)) {
                        command.notifyCommand(context, result);
                    } else if ("force".equals(result.remoteCommand)) {
                        command.forceCommand(context, result);
                    }
                }
            }
        }
    }
}

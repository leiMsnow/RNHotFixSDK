package ray.rn.hotfix.command;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ray.rn.hotfix.constants.FileConstant;
import ray.rn.hotfix.hotupdate.HotUpdate;
import ray.rn.hotfix.models.ResponseModels;

/**
 * Created by wecash on 2018/1/4.
 */

public class HotCommand implements ICommand {

    @Override
    public void notifyCommand(Activity activity, ResponseModels result) {
        Log.i(this.getClass().getName(), "notify");
        downAsyncFile(activity, result.remoteHotUpdateURL);
    }

    @Override
    public void forceCommand(Activity activity, ResponseModels result) {
        Log.i(this.getClass().getName(), "force");
        downAsyncFile(activity, result.remoteHotUpdateURL);
    }

    private void downAsyncFile(final Context context, String fileUrl) {
//        fileUrl = FileConstant.JS_BUNDLE_REMOTE_URL;
        if (TextUtils.isEmpty(fileUrl)) {
            return;
        }
        final String rootPath = FileConstant.getAppRoot(context);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(fileUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream;
                try {
                    String dirName = rootPath + FileConstant.TEMP_FOLDER;
                    File file = new File(dirName);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String fileName = dirName + FileConstant.ZIP_NAME;
                    File file1 = new File(fileName);
                    if (!file1.exists()) {
                        file1.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(fileName);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                    Log.d("downAsyncFile", "download success");
                    HotUpdate.handleZIP(rootPath);
                } catch (Exception e) {
                    Log.i("downAsyncFile", "IOException");
                    e.printStackTrace();
                }

            }
        });
    }
}

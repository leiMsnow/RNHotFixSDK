package com.ray.rn.hotfix.fix;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.ray.rn.hotfix.models.RequestModels;
import com.ray.rn.hotfix.models.ResponseModels;
import com.ray.rn.hotfix.utils.CompareUtils;
import com.ray.rn.hotfix.utils.IntenetUtil;
import com.ray.rn.hotfix.utils.SPUtils;
import com.ray.rn.hotfix.utils.Utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wecash on 2018/1/4.
 */

public class FixRequest {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static void request(Activity context) {
        RequestModels requestModels = new RequestModels();
        requestModels.sourceMark = "android";
        requestModels.deviceMacAddress = Utils.getMac();
        requestModels.localHotUpdateVersion = SPUtils.get(context, "HOT_UPDATE_VERSION",
                Utils.getAppVersionName(context)).toString();
        requestModels.networkType = IntenetUtil.getNetworkState(context);
        requestModels.deviceModel = Build.MODEL;
        requestModels.deviceIMEI = Utils.getIMEI(context);
        requestModels.systemVersion = Build.VERSION.SDK_INT + "";
        requestModels.localVersion = Utils.getAppVersionName(context);
        requestModels.sourceCode = "smartlaw";
        execute(context, requestModels);
    }

    private static void execute(final Activity context, final RequestModels requestModels) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = new Gson().toJson(requestModels);
                Log.i("FixRequest", json);
                RequestBody body = RequestBody.create(MEDIA_TYPE, json);
                Request request = new Request.Builder()
                        .url("http://mobile.wecash.net/versionControl")
                        .post(body)
                        .build();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    if (response != null) {
//                        Log.i("FixRequest", response.body().string());
                        ResponseModels result = new Gson().fromJson(response.body().charStream(), ResponseModels.class);
                        Log.i("FixRequest", result.toString());
                        FixResponse versionResponse = new FixResponse(context);
                        // 冷更新处理
                        String oldVersion = requestModels.localVersion;
                        String newVersion = result.remoteVersion;
                        if (CompareUtils.compareVersion(oldVersion, newVersion) == -1) {
                            Log.i("FixRequest", "versionResponse.coldCommand");
                            versionResponse.coldCommand(result);
                        }
                        // 热更新处理
//                        String oldHotVersion = requestModels.localHotUpdateVersion;
//                        String newHotVersion = result.remoteHotUpdateVersion;
//                        if (CompareUtils.compareVersion(oldHotVersion, newHotVersion) == -1) {
//                            versionResponse.hotCommand(result);
//                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

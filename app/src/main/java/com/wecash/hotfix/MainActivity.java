package com.wecash.hotfix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wecash.hotfix.models.VersionControl;
import com.wecash.hotfix.models.VersionResult;
import com.wecash.hotfix.utils.IntenetUtil;
import com.wecash.hotfix.utils.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private MyHandler myHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void getVersionControl() {
        myHandler = new MyHandler(this);
        VersionControl versionControl = new VersionControl();
        versionControl.sourceMark = "android";
        versionControl.deviceMacAddress = Utils.getMac();
        versionControl.localHotUpdateVersion = getSharedPreferences("Info", Context.MODE_PRIVATE)
                .getString("HOT_UPDATE_VERSION", Utils.getAppVersionName(this));
        versionControl.networkType = IntenetUtil.getNetworkState(this);
        versionControl.deviceModel = Build.MODEL;
        versionControl.deviceIMEI = Utils.getIMEI(this);
        versionControl.systemVersion = Build.VERSION.SDK_INT + "";
        versionControl.localVersion = Utils.getAppVersionName(this);
        versionControl.sourceCode = "smartlaw";
        versionControl(versionControl);
    }

    private void versionControl(final VersionControl versionControl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = new Gson().toJson(versionControl);
                Log.i("MainActivity", json);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url("http://mobile.wecash.net/versionControl")
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response != null) {
                        VersionResult result = new Gson().fromJson(response.body().charStream(), VersionResult.class);
                        Log.i("MainActivity", result.toString());

                        String oldVersion = versionControl.localVersion;
                        String newVersion = result.remoteVersion;
                        int version = compareVersion(oldVersion, newVersion);
                        if (version == -1) {
                            Message message = myHandler.obtainMessage();
                            message.obj = result;
                            message.what = 1;
                            myHandler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static int compareVersion(String oldVersion, String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return 0;
        }
        if (oldVersion.equals(newVersion)) {
            return 0;
        }

        String[] oldVersionArray = oldVersion.split("\\.");
        String[] newVersionArray = newVersion.split("\\.");

        int index = 0;
        int minLen = Math.min(oldVersionArray.length, newVersionArray.length);
        int diff = 0;

        while (index < minLen && (diff = Integer.parseInt(oldVersionArray[index]) - Integer.parseInt(newVersionArray[index])) == 0) {
            index++;
        }

        if (diff == 0) {
            for (int i = index; i < oldVersionArray.length; i++) {
                if (Integer.parseInt(oldVersionArray[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < newVersionArray.length; i++) {
                if (Integer.parseInt(newVersionArray[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = weakReference.get();
            if (activity != null) {
                if (msg.what == 1) {
                    final VersionResult result = (VersionResult) msg.obj;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                            .setTitle("Notify")
                            .setMessage(result.remoteVersionUpdateDescription);
                    if ("notify".equals(result.remoteCommand)) {
                        dialog.setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!TextUtils.isEmpty(result.remoteVersionUpdateURL)) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri uri = Uri.parse(result.remoteVersionUpdateURL);
                                            intent.setData(uri);
                                            activity.startActivity(intent);
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                    } else if ("force".equals(result.remoteCommand)) {
                        dialog.setCancelable(false)
                                .setPositiveButton("Update",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (!TextUtils.isEmpty(result.remoteVersionUpdateURL)) {
                                                    Intent intent = new Intent();
                                                    intent.setAction("android.intent.action.VIEW");
                                                    Uri uri = Uri.parse(result.remoteVersionUpdateURL);
                                                    intent.setData(uri);
                                                    activity.startActivity(intent);
                                                }

                                                activity.finish();
                                            }
                                        }).show();
                    }
                }
            }
        }
    }
}

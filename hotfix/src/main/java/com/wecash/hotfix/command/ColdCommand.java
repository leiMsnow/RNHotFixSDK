package com.wecash.hotfix.command;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.wecash.hotfix.models.ResponseModels;

/**
 * Created by wecash on 2018/1/4.
 */

public class ColdCommand implements ICommand {

    @Override
    public void notifyCommand(final Activity activity, final ResponseModels result) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Notify")
                .setMessage(result.remoteVersionUpdateDescription);
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
    }

    @Override
    public void forceCommand(final Activity activity, final ResponseModels result) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Notify")
                .setMessage(result.remoteVersionUpdateDescription);
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
                                System.exit(0);
                            }
                        }).show();
    }

}

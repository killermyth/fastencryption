package com.lvyingbin.fastencryption.util;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;

/**
 * Created by justin on 2015/2/14.
 */
public final class ActivityUtil {
    public static ActivityManager.RunningTaskInfo getForegroundActivity(Context paramContext)
    {
        List localList = ((ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if ((localList != null) && (localList.size() > 0)) {
            return (ActivityManager.RunningTaskInfo)localList.get(0);
        }
        return null;
    }

    /**
     * 弹窗
     * @param title
     * @param message
     */
    public static ProgressDialog showProgressDialog(Context context,String title, String message) {
        ProgressDialog progressDialog = ProgressDialog.show(context, title,message, true, false);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 弹窗隐藏
     */
    public static void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}

package com.lvyingbin.fastencryption.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lvyingbin.fastencryption.service.MonitorService;

/**
 * Created by justin on 2015/2/16.
 */
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        Log.e("BootReceiver", "BootReceiver start monitor");
        Toast.makeText(paramContext, "Boot Completed", Toast.LENGTH_LONG).show();
        Intent localIntent = new Intent();
        localIntent.setClass(paramContext, MonitorService.class);
        paramContext.startService(localIntent);
    }
}

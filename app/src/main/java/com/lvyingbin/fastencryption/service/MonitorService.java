package com.lvyingbin.fastencryption.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.lvyingbin.fastencryption.activity.LockScreenActivity;
import com.lvyingbin.fastencryption.util.AccessToken;
import com.lvyingbin.fastencryption.util.ActivityUtil;

import java.util.Set;

public class MonitorService extends Service implements IMonitorService{
    private final String LOG_TAG = "com.lvyingbin.fastencryption.service.MonitorService";
    public final static String BROADCAST_MONITOR_ACTION = "com.lvyingbin.fastencryption.service.MONITOR_ACTION";

    private boolean stop = false;
    private boolean isMonitorStarted = false;
    private AccessToken accessToken;
    private Context mContext;
    private final IBinder binder = new MonitorBinder();
    private BroadcastReceiver screenOnOffReceiver;


    @Override
    public void onCreate()
    {
        Log.e(LOG_TAG,"service create");
        mContext = MonitorService.this;
        accessToken = new AccessToken(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        screenOnOffReceiver = new screenOnOffReceiver();
        registerReceiver(screenOnOffReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MonitorBinder extends Binder {
        public MonitorService getService() {
            return MonitorService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isMonitorStarted){
            startMonitor();
        }
        return START_STICKY;
    }

    public void startMonitor(){
        AsyncTask<Void, Void, Void> taskMonitor = new AsyncTask<Void, Void, Void>(){
            Set<String> lockAppSet;
            String appHit = "";
            Boolean appLockFlag;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                lockAppSet = accessToken.getStrSetSharedPreferences("lockApp");
                appLockFlag = accessToken.getBoolSharedPreferences("appLockFlag");
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.e(LOG_TAG,"appLockFlag:"+appLockFlag);
                if(appLockFlag){
                    if(!lockAppSet.isEmpty()){
                        isMonitorStarted = true;
                        Log.e(LOG_TAG, "monitor started");
                        stop = false;
                        while(!stop) {
                            try{
                                ActivityManager.RunningTaskInfo taskInfo = ActivityUtil.getForegroundActivity(mContext);
                                if(!taskInfo.topActivity.getPackageName().equals("com.lvyingbin.fastencryption")){
                                    if(lockAppSet.contains(taskInfo.topActivity.getPackageName())){
                                        Log.e(LOG_TAG,"bingo");
                                        if(!appHit.equals(taskInfo.topActivity.getPackageName())){
                                            Log.e(LOG_TAG,"appHit"+appHit);
                                            appHit = taskInfo.topActivity.getPackageName();
                                            Intent intent = new Intent(mContext,LockScreenActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("activityGoal","unlock app");
                                            startActivity(intent);
                                        }
                                    }else{
                                        appHit = "";
                                    }
                                }
                                Log.e(LOG_TAG,taskInfo.topActivity.getPackageName());
                                Thread.sleep(2000);
                            }catch (Exception e) {
                                Log.e(LOG_TAG, "Exception : " + e);
                            }
                        }
                        Log.e(LOG_TAG, "monitor stopped");
                        isMonitorStarted = false;
                    }
                }
                return null;
    }
};
        taskMonitor.execute();
    }

    public void stopMonitor(){
        stop = true;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenOnOffReceiver);
        Log.e(LOG_TAG, "Service Destroyed.");
    }

    public class screenOnOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(LOG_TAG, "screenOnOffReceiver stop monitor");
                stopMonitor();
            }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.e(LOG_TAG, "screenOnOffReceiver start monitor");
                startMonitor();
            }
        }
    }

}

package com.lvyingbin.fastencryption.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lvyingbin.fastencryption.util.AccessToken;

import java.io.File;
import java.io.IOException;

/**
 * Created by justin on 2015/1/25.
 */
public class FastEncryption extends Application {
    public static final String TAG = "FastEncryption";
    public static final String PRIVACY_PATH = "/.FastEncryption/";
    public static final String NO_MEDIA = ".nomedia";
    public static final String CAUTION = "当前目录极重要，请谨慎操作";
    public static final String PRIVACY_IMG = ".privacyImg/";
    public static final String PRIVACY_VIDEO = ".privacyVideo/";
    public static final String PRIVACY_FILE = ".privacyFile/";
    private static String sdDir;
    AccessToken accessToken;

    @Override
    public void onCreate() {
        super.onCreate();
        Context mContext = this.getApplicationContext();
        accessToken = new AccessToken(mContext);
        sdDir = FileUtil.getSdDir();
        checkDir();
        Log.e(TAG, "Application onCreate");
    }

    public boolean getFirstRun() {
        return accessToken.getBoolSharedPreferences("firstRun");
    }

    public void setRan() {
        accessToken.writeBoolSharedPreferences("firstRun", false);
    }

    public static boolean checkDir() {
        if(!isDirExisted()){
            if (sdDir == null) {
                sdDir = FileUtil.getSdDir();
            }
            try {
                new File(sdDir + PRIVACY_PATH).mkdir();
                new File(sdDir + PRIVACY_PATH + NO_MEDIA).mkdir();
                new File(sdDir + PRIVACY_PATH + CAUTION).createNewFile();
                new File(sdDir + PRIVACY_PATH + PRIVACY_IMG).mkdir();
                new File(sdDir + PRIVACY_PATH + PRIVACY_VIDEO).mkdir();
                new File(sdDir + PRIVACY_PATH + PRIVACY_FILE).mkdir();
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }
        }
        return isDirExisted();
    }

    public static boolean isDirExisted(){
        return true & new File(sdDir + PRIVACY_PATH).exists() & new File(sdDir + PRIVACY_PATH + CAUTION).exists() & new File(sdDir + PRIVACY_PATH + PRIVACY_IMG).exists() & new File(sdDir + PRIVACY_PATH + PRIVACY_VIDEO).exists() & new File(sdDir + PRIVACY_PATH + PRIVACY_FILE).exists();
    }
}

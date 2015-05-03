package com.lvyingbin.fastencryption.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by justin on 2015/1/25.
 */
public class AccessToken {
    public static final String FastEncryption_SHARED_PREFERENCES_NAME = "FastEncryptionPrefs";
    private Context mContext;

    public AccessToken(Context mContext) {
        this.mContext = mContext;
    }

    public void writeStrSharedPreferences(String key, String val) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public String getStrSharedPreferences(String key){
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        return mPrefs.getString(key,"");
    }

    public void writeBoolSharedPreferences(String key, Boolean val) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public Boolean getBoolSharedPreferences(String key){
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        return mPrefs.getBoolean(key,true);
    }

    public void writeStrSetSharedPreferences(String key, Set<String> values) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public Set<String> getStrSetSharedPreferences(String key){
        SharedPreferences mPrefs = mContext.getSharedPreferences(FastEncryption_SHARED_PREFERENCES_NAME, 0);
        Set<String> set = new HashSet<String>();
        return mPrefs.getStringSet(key, set);
    }
}

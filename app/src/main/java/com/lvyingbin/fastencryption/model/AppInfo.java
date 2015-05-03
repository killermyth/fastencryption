package com.lvyingbin.fastencryption.model;

import android.graphics.drawable.Drawable;

/**
 * Created by justin on 2015/2/1.
 */
public class AppInfo {
    private String pkgName ;
    private String appLabel;
    private Drawable appIcon ;
    private Boolean lock;

    public AppInfo(String pkgName,String appLabel,Drawable appIcon,Boolean lock){
        setPkgName(pkgName);
        setAppLabel(appLabel);
        setAppIcon(appIcon);
        setLock(lock);
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }
}

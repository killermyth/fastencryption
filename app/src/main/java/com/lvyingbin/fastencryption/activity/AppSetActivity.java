package com.lvyingbin.fastencryption.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.model.AppInfo;
import com.lvyingbin.fastencryption.service.MonitorService;
import com.lvyingbin.fastencryption.util.AccessToken;
import com.lvyingbin.fastencryption.util.BrowseApplicationInfoAdapter;


public class AppSetActivity extends ActionBarActivity implements ActionBar.TabListener {
    private final String TAG = "AppSetActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private MonitorService monitorService;
    private MonitorService.MonitorBinder monitorBinder;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_set);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d93333")));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        Intent bindIntent = new Intent(this, MonitorService.class);
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "Service connected");
            monitorBinder = (MonitorService.MonitorBinder) service;
            monitorService = monitorBinder.getService();
            mBound = true;
            monitorService.stopMonitor();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        if (mBound) {
            Log.e(TAG, "stopMonitor");
            monitorService.stopMonitor();
        }
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (mBound) {
            Log.e(TAG, "startMonitor");
            monitorService.startMonitor();
        }
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";
        private Context mContext;
        private List<AppInfo> userAppList = new ArrayList<AppInfo>();
        private List<AppInfo> sysAppList = new ArrayList<AppInfo>();
        private BrowseApplicationInfoAdapter browseAppAdapter = null ;
        private ListView appListView;
        private AccessToken accessToken;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mContext = activity.getApplicationContext();
            accessToken = new AccessToken(mContext);
        }

         /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_app_set, container, false);
            appListView = (ListView) rootView.findViewById(R.id.appListView);
            getApp();
            if ((ARG_SECTION_NUMBER!= null) && (getArguments().getInt(ARG_SECTION_NUMBER) == 1) ){
                browseAppAdapter = new BrowseApplicationInfoAdapter(mContext, userAppList);
                appListView.setAdapter(browseAppAdapter);
            }else if((ARG_SECTION_NUMBER!= null) && (getArguments().getInt(ARG_SECTION_NUMBER) == 2) ){
                browseAppAdapter = new BrowseApplicationInfoAdapter(mContext, sysAppList);
                appListView.setAdapter(browseAppAdapter);
            }
            return rootView;
        }

        //准备列表中所需数据
        private void getApp() {
            PackageManager packageManager = mContext.getPackageManager();
//            List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//            Collections.sort(packages, new ApplicationInfo.DisplayNameComparator(packageManager));
//            for (ApplicationInfo packageInfo : packages) {
//                String pkgName = packageInfo.packageName;
//                String appLabel = (String) packageInfo.loadLabel(packageManager);
//                Drawable appIcon = packageInfo.loadIcon(packageManager);
//                Boolean lock = false;
//                AppInfo appInfo = new AppInfo(pkgName,appLabel,appIcon,lock);
//                Log.e(TAG,appLabel);
////                Log.e(TAG, "" + packageManager.getApplicationIcon(packageInfo));
////                map.put("appLabel", packageManager.getApplicationLabel(packageInfo));
//                if(isSystemPackage(packageInfo)){
//                        sysAppList.add(appInfo);
//                }else{
//                    userAppList.add(appInfo);
//                }
//            }
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(mainIntent, 0);
            Collections.sort(resolveInfo,new ResolveInfo.DisplayNameComparator(packageManager));
            Set<String> lockAppSet = accessToken.getStrSetSharedPreferences("lockApp");
            for (ResolveInfo reInfo : resolveInfo){
                String pkgName = reInfo.activityInfo.packageName;
                String appLabel = (String) reInfo.loadLabel(packageManager);
                Drawable appIcon = reInfo.loadIcon(packageManager);
                Boolean lock = false;
                if(lockAppSet.contains(pkgName)){
                    lock = true;
                }
                AppInfo appInfo = new AppInfo(pkgName,appLabel,appIcon,lock);
                if(isSystemPackage(reInfo)){
                    sysAppList.add(appInfo);
                }else{
                    userAppList.add(appInfo);
                }
            }
        }
        //判断应用程序是否为系统自带
        private boolean isSystemPackage(ResolveInfo resolveInfo) {
            return (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        }
    }

}
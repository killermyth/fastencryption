package com.lvyingbin.fastencryption.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.model.AppInfo;

import java.util.List;
import java.util.Set;

/**
 * Created by justin on 2015/2/1.
 */
//自定义适配器类，提供给listView的自定义view
public class BrowseApplicationInfoAdapter extends BaseAdapter {
    private List<AppInfo> mlistAppInfo = null;
    private Context mContext;
    private AccessToken accessToken;

    LayoutInflater infater = null;

    public BrowseApplicationInfoAdapter(Context context,  List<AppInfo> apps) {
        infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlistAppInfo = apps ;
        mContext = context;
        accessToken = new AccessToken(mContext);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + mlistAppInfo.size());
        return mlistAppInfo.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistAppInfo.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup arg2) {
        System.out.println("getView at " + position);
        View view = null;
        ViewHolder holder = null;
        if (convertview == null || convertview.getTag() == null) {
            view = infater.inflate(R.layout.app_set_listview, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertview ;
            holder = (ViewHolder) convertview.getTag() ;
        }
        final AppInfo appInfo = (AppInfo) getItem(position);
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvAppLabel.setText(appInfo.getAppLabel());
        if(appInfo.getLock()){
            holder.tvAppState.setImageResource(R.drawable.applock);
        }else{
            holder.tvAppState.setImageResource(R.drawable.appunlock);
        }
        final ViewHolder finalHolder = holder;
        finalHolder.tvAppState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.e("","click imageView");
                Set<String> lockAppSet = accessToken.getStrSetSharedPreferences("lockApp");
                if(appInfo.getLock()){
                    appInfo.setLock(false);
                    finalHolder.tvAppState.setImageResource(R.drawable.appunlock);
                    lockAppSet.remove(appInfo.getPkgName());
                    accessToken.writeStrSetSharedPreferences("lockApp",lockAppSet);
                }else{
                    appInfo.setLock(true);
                    finalHolder.tvAppState.setImageResource(R.drawable.applock);
                    lockAppSet.add(appInfo.getPkgName());
                    accessToken.writeStrSetSharedPreferences("lockApp",lockAppSet);
                }
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView tvAppLabel;
        ImageView tvAppState;

        public ViewHolder(View view) {
            this.appIcon = (ImageView) view.findViewById(R.id.appIcon);
            this.tvAppLabel = (TextView) view.findViewById(R.id.appLabel);
            this.tvAppState = (ImageView) view.findViewById(R.id.appState);
        }
    }
}

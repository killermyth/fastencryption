package com.lvyingbin.fastencryption.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.activity.ImgSelectActivity;
import com.lvyingbin.fastencryption.model.AppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by justin on 2015/2/24.
 */
//自定义适配器类，提供给listView的自定义view
public class ImgFolderInfoAdapter extends BaseAdapter {
    private static final String TAG = "ImgFolderInfoAdapter";
    private ArrayList<Bundle> imgFolderList = null;
    private Context mContext;
    private AccessToken accessToken;

    LayoutInflater infater = null;

    public ImgFolderInfoAdapter(Context context, ArrayList<Bundle> imgFolders) {
        infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgFolderList = imgFolders ;
        mContext = context;
        accessToken = new AccessToken(mContext);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + imgFolderList.size());
        return imgFolderList.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imgFolderList.get(position);
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
            view = infater.inflate(R.layout.list_item_img_folder, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertview ;
            holder = (ViewHolder) convertview.getTag() ;
        }
        final Bundle imgFolderInfo = (Bundle) getItem(position);
        String str = imgFolderInfo.getString("_data");
//        attempt first time
//        Uri uri = Uri.parse(str);
//        holder.image.setImageURI(uri);
//        attempt second time
//        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(str), 36, 36, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        holder.image.setImageBitmap(ImgDisplayUtil.decodeSampledBitmapFromFile(str, 36, 36));
        holder.folder.setText(imgFolderInfo.getString("bucket_display_name"));
        holder.count.setText(String.valueOf(imgFolderInfo.getInt("count")));
        return view;
    }

    class ViewHolder {
        ImageView image;
        TextView folder;
        TextView count;

        public ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.image);
            this.folder = (TextView) view.findViewById(R.id.folder);
            this.count = (TextView) view.findViewById(R.id.count);
        }
    }
}

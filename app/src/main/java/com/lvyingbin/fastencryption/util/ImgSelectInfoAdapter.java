package com.lvyingbin.fastencryption.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.activity.ImgSelectActivity;
import com.lvyingbin.fastencryption.activity.ImgSetActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by justin on 2015/2/24.
 */
//自定义适配器类，提供给gridView的自定义view
public class ImgSelectInfoAdapter extends BaseAdapter {
    private static final String TAG = "ImgSelectInfoAdapter";
    private ArrayList imgSelectList = null;
    private Context mContext;
    private AccessToken accessToken;
    public SparseBooleanArray isChecked;
    private LinearLayout bottom_button_bar;

    LayoutInflater infater = null;

    public ImgSelectInfoAdapter(Context context, ArrayList imgSelect,LinearLayout bottom_button_bar) {
        infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgSelectList = imgSelect;
        mContext = context;
        accessToken = new AccessToken(mContext);
        isChecked = new SparseBooleanArray();
        this.bottom_button_bar = bottom_button_bar;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + imgSelectList.size());
        return imgSelectList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imgSelectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup arg2) {
        System.out.println("getView at " + position);
        View view = null;
        ViewHolder holder = null;
        if (convertview == null || convertview.getTag() == null) {
            view = infater.inflate(R.layout.grid_item_img_select, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) convertview.getTag();
        }
        if (isChecked.get(position)) {
            holder.cb_image_item.setChecked(true);
        } else {
            holder.cb_image_item.setChecked(false);
        }
        final HashMap imgInfo = (HashMap) getItem(position);
        String str = imgInfo.get("_data").toString();
        if(mContext.getClass().equals(ImgSelectActivity.class)){
            holder.image.setImageBitmap(ImgDisplayUtil.decodeSampledBitmapFromFile(str, 96, 72));
        }else if(mContext.getClass().equals(ImgSetActivity.class)){
            Bitmap bitmap = ImgDisplayUtil.decodeEncryptedBitmapFromFile(str, 96, 72);
            Log.e(TAG,"123456"+bitmap);
            holder.image.setImageBitmap(ImgDisplayUtil.decodeEncryptedBitmapFromFile(str, 96, 72));
        }
//        holder.image.setImageBitmap(ImgDisplayUtil.decodeSampledBitmapFromFile(str, 96, 72));
        final CheckBox checkbox = holder.cb_image_item;
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                    isChecked.delete(position);
                    if(isChecked.size()==0){
                        bottom_button_bar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if(isChecked.size()==0){
                        bottom_button_bar.setVisibility(View.VISIBLE);
                    }
                    checkbox.setChecked(true);
                    isChecked.append(position, true);
                }
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView image;
        CheckBox cb_image_item;
        FrameLayout clickLayout;

        public ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.image);
            this.cb_image_item = (CheckBox) view.findViewById(R.id.cb_image_item);
            this.clickLayout = (FrameLayout) view.findViewById(R.id.clickLayout);
        }
    }

    public SparseBooleanArray getIsChecked(){
        return isChecked;
    }
}

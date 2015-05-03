package com.lvyingbin.fastencryption.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.util.ImgDisplayUtil;
import com.lvyingbin.fastencryption.util.ImgFolderInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImgFolderActivity extends ActionBarActivity {
    private final String TAG = "ImgFolderActivity";
    private ListView listview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_floder);
        mContext = this;
        listview = (ListView) this.findViewById(R.id.item_imgFolder);
        showImgFolder();
    }

    public void showImgFolder(){
        AsyncTask<Void, Void, ArrayList<Bundle>> taskShowImgFolder = new AsyncTask<Void, Void, ArrayList<Bundle>>(){
            @Override
            protected void onPreExecute(){
//                ImgDisplayUtil.allScan(mContext);
            }

            @Override
            protected ArrayList<Bundle> doInBackground(Void... params) {
                ArrayList localArrayList = new ArrayList();
                Cursor localCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { "_data", "bucket_display_name", "bucket_id" }, "_size > 0  and _data is not null  and _data <> '' ", null, "date_modified desc ");
                if ((localCursor != null)&&(localCursor.getCount()>0)){
                    HashMap localHashMap = new HashMap();
                    HashSet localHashSet = new HashSet();
                    while(localCursor.moveToNext()){
                        String str1,str2,str3;
                        int i1 = localCursor.getColumnIndex("bucket_id");
                        int i2 = localCursor.getColumnIndex("bucket_display_name");
                        int i3 = localCursor.getColumnIndex("_data");
                        str1 = localCursor.getString(i3);
                        str2 = localCursor.getString(i1);
                        str3 = localCursor.getString(i2);
                        if (localHashSet.contains(str1)) {
                            continue;
                        }
                        File localFile = new File(str1);
                        if ((localFile.exists()) && (!localFile.isDirectory()) && (localFile.length() > 0L)){
                            if (!localHashMap.containsKey(str2)) {
                                localHashSet.add(str1);
                                Bundle localBundle1 = new Bundle();
                                localBundle1.putInt("count", 1);
                                localBundle1.putString("_data", str1);
                                localBundle1.putString("bucket_id", str2);
                                localBundle1.putString("bucket_display_name", str3);
                                localHashMap.put(str2, localBundle1);
                            }else{
                                Bundle localBundle2 = (Bundle)localHashMap.get(str2);
                                localBundle2.putInt("count", 1 + localBundle2.getInt("count"));
                            }
                        }
                    }
                    localArrayList.addAll(localHashMap.values());
                    localCursor.close();
                }
                return localArrayList;
            }

            @Override
            protected void onPostExecute(ArrayList<Bundle> result) {
                ImgFolderInfoAdapter adapter = new ImgFolderInfoAdapter(mContext,result);
                listview.setAdapter(adapter);
                final ArrayList<Bundle> finalBundle = result;
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Bundle localBundle = finalBundle.get(position);
                        Intent intent = new Intent();
                        intent.setClass(mContext, ImgSelectActivity.class);
                        intent.putExtra("bucket_id", localBundle.getString("bucket_id"));
                        intent.putExtra("bucket_display_name", localBundle.getString("bucket_display_name"));
                        startActivityForResult(intent, 100);
                    }
                });
            }
        };
        taskShowImgFolder.execute();
    }


}

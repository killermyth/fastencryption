package com.lvyingbin.fastencryption.activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.util.AESCrypt;
import com.lvyingbin.fastencryption.util.BitmapUtil;
import com.lvyingbin.fastencryption.util.FastEncryption;
import com.lvyingbin.fastencryption.util.FileUtil;
import com.lvyingbin.fastencryption.util.ImgDisplayUtil;
import com.lvyingbin.fastencryption.util.ImgSelectInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImgSelectActivity extends ActionBarActivity {
    private final String TAG = "ImgSelectActivity";
    private Context mContext;
    private GridView item_grid;
    private Button cancel_btn,hide_btn;
    private LinearLayout bottom_button_bar;
    private ImgSelectInfoAdapter adapter;
    private ArrayList imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_select);
        mContext = this;
        initCom();
        controlEvents();
        showImg();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_img_select, menu);
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

    private void initCom(){
        item_grid = (GridView)findViewById(R.id.item_grid);
        cancel_btn = (Button)findViewById(R.id.cancel_btn);
        hide_btn = (Button)findViewById(R.id.hide_btn);
        bottom_button_bar = (LinearLayout)findViewById(R.id.bottom_button_bar);
    }

    private void controlEvents(){
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        hide_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SparseBooleanArray isChecked = adapter.getIsChecked();
                Log.e(TAG,""+isChecked.size());
                for (int i = 0; i < isChecked.size(); i++){
                    if(isChecked.valueAt(i)){
                        HashMap imgInfo = (HashMap) imgList.get(isChecked.keyAt(i));
                        Log.e(TAG,imgInfo.get("_data").toString());
//                        Bitmap bitmap = BitmapUtil.getBitmapFromPath(imgInfo.get("_data").toString());
                        String imagePath = FileUtil.getDirPath(1);
//                        BitmapUtil.saveBitmapToPath(imagePath+"123",bitmap);
                        AESCrypt.crypt(imgInfo.get("_data").toString(),imagePath+"345", FastEncryption.TAG);
                    }
                }
            }
        });
    }

    private void showImg(){
        AsyncTask<Void, Void, ArrayList> task = new AsyncTask<Void, Void, ArrayList>(){
            @Override
            protected void onPreExecute(){
//                ImgDisplayUtil.allScan(mContext);
            }

            @Override
            protected ArrayList doInBackground(Void... params) {
                ArrayList arraylist = new ArrayList();
                HashSet hashset = new HashSet();
                String bucket_id = getIntent().getStringExtra("bucket_id");
                Log.e(TAG,bucket_id);
                String s2;
                String as[];
                Cursor cursor;
                if (TextUtils.isEmpty(bucket_id))
                {
                    s2 = "_size > 0  and _data is not null  and _data <> '' ";
                    as = null;
                } else
                {
                    s2 = "_size > 0 and _data is not null and _data <> '' and bucket_id = ?";
                    as = (new String[] {
                            bucket_id
                    });
                }
                cursor = mContext.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {"_data"}, s2, as, "date_modified desc ");
                if (cursor != null)
                {
                    while(cursor.moveToNext())
                    {
                        int i1 = cursor.getColumnIndex("_data");
                        String s3 = cursor.getString(i1);
                        if (!hashset.contains(s3))
                        {
                            File file = new File(s3);
                            boolean flag;
                            if (file.exists() && !file.isDirectory() && file.length() > 0L)
                            {
                                flag = true;
                            } else
                            {
                                flag = false;
                            }
                            if (flag)
                            {
                                hashset.add(s3);
                                HashMap hashmap = new HashMap();
                                hashmap.put("_data", s3);
                                arraylist.add(hashmap);
                            }
                        }
                    }
                }
                cursor.close();
                return arraylist;
            }

            @Override
            protected void onPostExecute(ArrayList result) {
                Log.e(TAG,""+result.size());
                adapter = new ImgSelectInfoAdapter(mContext,result,bottom_button_bar);
                item_grid.setAdapter(adapter);
                imgList = result;
            }
        };
        task.execute();
    }
}

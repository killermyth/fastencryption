package com.lvyingbin.fastencryption.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.util.FileUtil;
import com.lvyingbin.fastencryption.util.ImgDisplayUtil;
import com.lvyingbin.fastencryption.util.ImgSelectInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ImgSetActivity extends ActionBarActivity {
    private final String TAG = "ImgSetActivity";
    private Button btn_AddMedia,btn_delete,btn_selectAll,btn_restore;
    private LinearLayout layout_second;
    private GridView item_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_set);
        initCom();
        controlEvents();
        showEncryptedMedia();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_img_privacy, menu);
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d93333")));
        item_grid = (GridView)findViewById(R.id.item_grid);
        btn_AddMedia = (Button)findViewById(R.id.btn_AddMedia);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_selectAll = (Button)findViewById(R.id.btn_selectAll);
        btn_restore = (Button)findViewById(R.id.btn_restore);
        layout_second = (LinearLayout)findViewById(R.id.layout_second);
        showAddMedia();
    }

    private void showAddMedia(){
        btn_AddMedia.setText(R.string.addImg);
        btn_AddMedia.setVisibility(View.VISIBLE);
        layout_second.setVisibility(View.INVISIBLE);
    }

    private void showOther(){
        btn_AddMedia.setVisibility(View.INVISIBLE);
        layout_second.setVisibility(View.VISIBLE);
    }

    private void controlEvents(){
        btn_AddMedia.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent = new Intent(ImgSetActivity.this,ImgFolderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showEncryptedMedia(){
        String imagePath = FileUtil.getDirPath(1);
        File[] files = new File(imagePath).listFiles();
        ArrayList arraylist = new ArrayList();
        if (files != null)
        {
            for(File file:files){
                HashMap hashmap = new HashMap();
                hashmap.put("_data", file.getAbsolutePath());
                arraylist.add(hashmap);
            }
            ImgSelectInfoAdapter adapter = new ImgSelectInfoAdapter(this,arraylist,layout_second);
            item_grid.setAdapter(adapter);
        }

    }
}

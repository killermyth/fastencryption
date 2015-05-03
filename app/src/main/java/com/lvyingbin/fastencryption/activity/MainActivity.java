package com.lvyingbin.fastencryption.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.util.AccessToken;

public class MainActivity extends ActionBarActivity {
    private final String TAG="MainActivity";
    private String settingNum="";
    private TextView lockSwitcher;
    private LinearLayout appSet,imgSet;
    private AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessToken = new AccessToken(this);
        initCom();
        controlEvents();
    }

    private void initCom(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d93333")));
        lockSwitcher = (TextView)findViewById(R.id.lockSwitcher);
        appSet = (LinearLayout)findViewById(R.id.appSet);
        imgSet = (LinearLayout)findViewById(R.id.imgSet);
    }

    private void controlEvents(){
        lockSwitcher.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Boolean appLockFlag = accessToken.getBoolSharedPreferences("appLockFlag");
                accessToken.writeBoolSharedPreferences("appLockFlag",!appLockFlag);
            }
        });

        appSet.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent = new Intent(MainActivity.this,AppSetActivity.class);
                startActivity(intent);
            }
        });

        imgSet.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent = new Intent(MainActivity.this,ImgSetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

package com.lvyingbin.fastencryption.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvyingbin.fastencryption.R;
import com.lvyingbin.fastencryption.service.MonitorService;
import com.lvyingbin.fastencryption.util.AccessToken;
import com.lvyingbin.fastencryption.util.FastEncryption;


public class LockScreenActivity extends Activity {
    private final String TAG="LockScreenActivity";
    private TextView lock_screen_text,dialNum1,dialNum2,dialNum3,dialNum4,dialNum5,dialNum6,dialNum7,dialNum8,dialNum9,dialNum0;
    private ImageView lockScreen_4point1,lockScreen_4point2,lockScreen_4point3,lockScreen_4point4;
    private ImageView[] lockScreen_4pointArray= new ImageView[4];
    private String unlockNum="";
    private String firstUnlockNum="";
    private Boolean isSetFlag;
    private AccessToken accessToken;
    private String activityGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
        accessToken = new AccessToken(this);
        initCom();
        setControlEvents();
        runTimeCheck();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getActivityGoal();
    }

    private void getActivityGoal(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            activityGoal = extras.getString("activityGoal");
        }else{
            activityGoal = "";
        }
        Log.e(TAG,"activityGoal:"+activityGoal);
    }

    private void initCom(){
        lock_screen_text = (TextView)findViewById(R.id.lock_screen_text);
        lockScreen_4point1 = (ImageView)findViewById(R.id.lockScreen_4point1);
        lockScreen_4point2 = (ImageView)findViewById(R.id.lockScreen_4point2);
        lockScreen_4point3 = (ImageView)findViewById(R.id.lockScreen_4point3);
        lockScreen_4point4 = (ImageView)findViewById(R.id.lockScreen_4point4);
        lockScreen_4pointArray[0]=lockScreen_4point1;
        lockScreen_4pointArray[1]=lockScreen_4point2;
        lockScreen_4pointArray[2]=lockScreen_4point3;
        lockScreen_4pointArray[3]=lockScreen_4point4;
        dialNum1 = (TextView)findViewById(R.id.dialNum1);
        dialNum2 = (TextView)findViewById(R.id.dialNum2);
        dialNum3 = (TextView)findViewById(R.id.dialNum3);
        dialNum4 = (TextView)findViewById(R.id.dialNum4);
        dialNum5 = (TextView)findViewById(R.id.dialNum5);
        dialNum6 = (TextView)findViewById(R.id.dialNum6);
        dialNum7 = (TextView)findViewById(R.id.dialNum7);
        dialNum8 = (TextView)findViewById(R.id.dialNum8);
        dialNum9 = (TextView)findViewById(R.id.dialNum9);
        dialNum0 = (TextView)findViewById(R.id.dialNum0);
    }

    protected void setControlEvents(){
        dialNum1.setOnClickListener(handleClick);
        dialNum2.setOnClickListener(handleClick);
        dialNum3.setOnClickListener(handleClick);
        dialNum4.setOnClickListener(handleClick);
        dialNum5.setOnClickListener(handleClick);
        dialNum6.setOnClickListener(handleClick);
        dialNum7.setOnClickListener(handleClick);
        dialNum8.setOnClickListener(handleClick);
        dialNum9.setOnClickListener(handleClick);
        dialNum0.setOnClickListener(handleClick);
    }

    protected OnClickListener handleClick = new OnClickListener(){
        public  void onClick(View arg0){
            TextView textView = (TextView)arg0;
            String textNum = (String) textView.getText();
            unlockNum+=textNum;
            pointDraw();
            if(unlockNum.length()==4){
                Log.e(TAG, unlockNum);
                unlockNumCheck();
            }
        }
    };

    private void runTimeCheck(){
        if(((FastEncryption) getApplication()).getFirstRun()){
            //此处为首次运行,可以弹出一次使用帮助，同时启动监测服务，同时需要将程序锁默认打开，同时也需要进行密码设置
            //第一次启动监测服务
            Intent localIntent = new Intent();
            localIntent.setClass(this, MonitorService.class);
            startService(localIntent);
            accessToken.writeBoolSharedPreferences("appLockFlag",true);
            isSetFlag=true;
            lock_screen_text.setText("请设置解锁密码");
        }else{
            //此处启动需要进行解锁
            isSetFlag=false;
        }
    }

    /**
     * 验证解锁密码
     * if(isSetFlag为true)，进行首次密码设置
     * else 进行输入密码的验证
     */
    private void unlockNumCheck(){
        if(isSetFlag){
            if(firstUnlockNum.isEmpty()){
                firstUnlockNum = unlockNum;
                unlockNum = "";
                pointDraw();
                lock_screen_text.setText("请再次输入");
            }else{
                if(firstUnlockNum.equals(unlockNum)){
                    Log.e(TAG, "设置密码成功"+unlockNum);
                    //保存设置好的密码,跳转，设置首次运行成功
                    accessToken.writeStrSharedPreferences("unlockNum",unlockNum);
                    ((FastEncryption) getApplication()).setRan();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    firstUnlockNum="";
                    unlockNum="";
                    pointDraw();
                    Log.e(TAG, "设置密码失败"+unlockNum);
                    lock_screen_text.setText("重新设置密码");
                }
            }
        }else{
            String unlockNumSet = accessToken.getStrSharedPreferences("unlockNum");
            Log.e(TAG,activityGoal);
            if(unlockNumSet.equals(unlockNum)){
                if(activityGoal.isEmpty()){
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    Log.e(TAG,""+1);
                    finish();
                }else if(activityGoal.equals("unlock app")){
                    Log.e(TAG,""+2);
                    finish();
                }
            }else{
                unlockNum = "";
                pointDraw();
                lock_screen_text.setText("重新输入密码");
            }
        }
    }

    /**
     * 处理数字点击后的上方四个小圆圈的显示
     * 根据unlockNum的长度可以判断出画点的个数
     */
    private void pointDraw(){
        if(unlockNum.length()==0){
            for(ImageView lockScreenPoint:lockScreen_4pointArray){
                lockScreenPoint.setImageResource(R.drawable.lockscreen_4point);
            }
        }else{
            lockScreen_4pointArray[unlockNum.length()-1].setImageResource(R.drawable.lockscreen_4point_clicked);
        }
    }
    //返回android中app launch界面
    private void goHome()
    {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.HOME");
        startActivity(localIntent);
        finish();
    }

    public boolean onKeyDown(int j, KeyEvent keyevent)
    {
        goHome();
        return true;
    }
}

package com.sl.notification;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int NOTIFICATION_ID = 1;
    private final int NOTIFICATION_ICON_LEVEL = 1;
    private Button mBtn1;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suo_activity);
        findViews();
        initData();
    }

    private void findViews() {
        mBtn1 = (Button) findViewById(R.id.suo_btn1);

        mBtn1.setOnClickListener(this);
    }

    private void initData() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    int number = 0;
    private void showNormalNotification() {
        number++;
//        NOTIFICATION_ID++;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Suo的通知标题栏")
                .setContentText("Suo的通知内容栏")
//                .setAutoCancel(true)
//                .setTicker("消息来啦")
                .setNumber(number)
//                .setOngoing(true)//处于进行中的通知 有些系统在移除时会给提示(华为) 有些则移除不了(小米/三星)
//                .setAutoCancel(true)//点击后即移除
//                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.comm_ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.comm_ic_notification))
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_ONGOING_EVENT|Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        boolean enable = isNotificationEnabled(this);
        boolean apiEnable = NotificationManagerCompat.from(this).areNotificationsEnabled();
        if (enable || apiEnable) {
            Toast.makeText(this, "通知允许状态" , Toast.LENGTH_SHORT).show();
        } else {
            mBtn1.setText("通知禁止");
            Toast.makeText(this, "通知禁止状态" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suo_btn1:
                showNormalNotification();
                break;
        }
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context){

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int)opPostNotificationValue.get(Integer.class);
            return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}

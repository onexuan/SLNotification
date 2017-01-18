package com.sl.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.sl.notification.broadcast.NotificationBroadcastReceiver;

/**
 * Created by wuhongqi on 17/1/10.
 */
public class CustomNotificationActivity extends Activity implements View.OnClickListener {

    private int NOTIFICATION_ID = 2;

    private NotificationManager mNotificationManager;

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    private int number = 0;

    private SystemNotification mSystemNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_notification);
        findViews();
        initData();

        String tips = getIntent().getStringExtra("suoluo");
        if (!TextUtils.isEmpty(tips))
            Toast.makeText(this, "点击了自定义通知的"+tips, Toast.LENGTH_SHORT).show();
    }

    private void findViews() {
        mBtn1 = (Button) findViewById(R.id.btn_custom1);
        mBtn2 = (Button) findViewById(R.id.btn_custom2);
        mBtn3 = (Button) findViewById(R.id.btn_custom3);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
    }

    private void initData() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     *
     * @param useSystem 是否适配系统字体大小，颜色
     */
    private void showCustomNotification(boolean useSystem) {
        number++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(false);
//        builder.setOngoing(true);
        builder.setShowWhen(false);
        builder.setSmallIcon(R.drawable.comm_ic_notification);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.custom_1);
        remoteViews.setTextViewText(R.id.tv_title, "我是简单自定义标题");
        remoteViews.setTextViewText(R.id.tv_content, "我是自定义标题" + number);
//        remoteViews.setTextViewText(R.id.tv_time, "21:00");
        remoteViews.setImageViewResource(R.id.iv_custom, R.drawable.android);
        remoteViews.setImageViewResource(R.id.iv_logo, R.drawable.kg_ic_playing_bar_play_default);

        if(useSystem) {
            remoteViews.setTextViewText(R.id.tv_title, "我是跟随系统文字的通知标题");
            getSystemText();
            remoteViews.setTextColor(R.id.tv_title, mSystemNotification.titleColor);
            remoteViews.setTextColor(R.id.tv_content, mSystemNotification.contentColor);
            remoteViews.setTextColor(R.id.tv_time, mSystemNotification.contentColor);

            if (Build.VERSION.SDK_INT >= 16) {
                remoteViews.setTextViewTextSize(R.id.tv_title, TypedValue.COMPLEX_UNIT_PX, mSystemNotification.titleSize);
                remoteViews.setTextViewTextSize(R.id.tv_content, TypedValue.COMPLEX_UNIT_PX, mSystemNotification.contentSize);
                remoteViews.setTextViewTextSize(R.id.tv_time, TypedValue.COMPLEX_UNIT_PX, mSystemNotification.contentSize);
            }
        }

        Intent icon = new Intent(this, CustomNotificationActivity.class);
        icon.putExtra("suoluo", "icon");
        PendingIntent picon =  PendingIntent.getActivity(this, 1, icon, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_custom, picon);

        Intent logo = new Intent(this, CustomNotificationActivity.class);
        logo.putExtra("suoluo", "logo");
        PendingIntent plogo =  PendingIntent.getActivity(this, 2,logo, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_logo, plogo);

        Intent title = new Intent(this, CustomNotificationActivity.class);
        title.putExtra("suoluo", "title");
        PendingIntent ptitle =  PendingIntent.getActivity(this, 3,title, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_title, ptitle);

        builder.setContent(remoteViews);
        mNotificationManager.notify(NOTIFICATION_ID + number, builder.build());
    }

    public class SystemNotification {
        int titleColor;
        float titleSize;
        int contentColor;
        float contentSize;
        int iconWidth;
        int iconHeight;
    }

    public SystemNotification getSystemText() {
        mSystemNotification = new SystemNotification();

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("SLNOTIFICATION_TITLE")
                   .setContentText("SLNOTIFICATION_TEXT")
                   .setSmallIcon(R.drawable.comm_ic_notification)
                   .build();

            LinearLayout group = new LinearLayout(this);
            RemoteViews tempView = builder.getNotification().contentView;
            ViewGroup event = (ViewGroup) tempView.apply(this, group);
            recurseGroup(event);
            group.removeAllViews();
        } catch (Exception e) {
            mSystemNotification.titleColor = Color.BLACK;
            mSystemNotification.titleSize = 32;
            mSystemNotification.contentColor = Color.BLACK;
            mSystemNotification.contentSize = 24;
        }


        return mSystemNotification;
    }

    private boolean recurseGroup(ViewGroup gp) {
        for (int i = 0; i < gp.getChildCount(); i++) {
            View v = gp.getChildAt(i);
            if (v instanceof TextView) {
                final TextView text = (TextView) v;
                final String szText = text.getText().toString();
                if ("SLNOTIFICATION_TITLE".equals(szText)) {
                    mSystemNotification.titleColor = text.getTextColors().getDefaultColor();
                    mSystemNotification.titleSize = text.getTextSize();
//                    return true;
                }
                if ("SLNOTIFICATION_TEXT".equals(szText)) {
                    mSystemNotification.contentColor = text.getTextColors().getDefaultColor();
                    mSystemNotification.contentSize = text.getTextSize();
//                    return true;
                }
            }
//            if (v instanceof ImageView) {
//                final ImageView image = (ImageView) v;
//                if (image.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.comm_ic_notification))) {
//                    mSystemNotification.iconWidth = image.getWidth();
//                    mSystemNotification.iconHeight = image.getHeight();
//                }
//            }
            if (v instanceof ViewGroup) {// 如果是ViewGroup 遍历搜索
                recurseGroup((ViewGroup) gp.getChildAt(i));
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_custom1:
                showCustomNotification(false);
                break;
            case R.id.btn_custom2:
                showCustomNotification(true);
                break;
            case R.id.btn_custom3:
//                showCustomNotification(false);
                break;
        }
    }
}

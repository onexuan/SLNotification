package com.sl.notification;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sl.notification.broadcast.NotificationBroadcastReceiver;

/**
 * Created by wuhongqi on 17/1/9.
 */
public class NormalNotificationActivity extends AppCompatActivity implements View.OnClickListener{

    private int NOTIFICATION_ID = 1;
    private String NOTIFICATION_TAG = "notification";
    private final int NOTIFICATION_ICON_LEVEL = 1;
    private NotificationManager mNotificationManager;
    private int number = 0;

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;
    private Button mBtn8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_notification);
        findViews();
        initData();

        String tips = getIntent().getStringExtra("suoluo");
        if (!TextUtils.isEmpty(tips))
            Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }

    private void findViews() {
        mBtn1 = (Button) findViewById(R.id.btn_normal1);
        mBtn2 = (Button) findViewById(R.id.btn_normal2);
        mBtn3 = (Button) findViewById(R.id.btn_normal3);
        mBtn4 = (Button) findViewById(R.id.btn_normal4);
        mBtn5 = (Button) findViewById(R.id.btn_normal5);
        mBtn6 = (Button) findViewById(R.id.btn_normal6);
        mBtn7 = (Button) findViewById(R.id.btn_normal7);
        mBtn8 = (Button) findViewById(R.id.btn_normal8);


        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
    }

    private void initData() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    private void showNormalNotification() {
        showNotification(false, false, -1, 0, false, false);
    }

    private void showOnGongNotification() {
        showNotification(true, false, -1, 0, false, false);
    }

    private void showNeedDeleteNotification() {
        showNotification(false, true, -1, 0, false, false);
    }

    private void showStyleNotification() {
        String[] styles = {"InboxStyle", "BigTextStyle", "BigPictureStyle", "MediaStyle"};
        new AlertDialog.Builder(this).setTitle("请选择Style的类型").setItems(styles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showNotification(false, false, which, 0, false, false);
            }
        }).create().show();
    }

    private void showProgressNotification() {
        showNotification(false, false, -1, 0, true, false);
    }

    private void showMaxPriorityNotification() {
        showNotification(false, false, -1, 2, false, false);
    }

    private void showMinPriorityNotification() {
        showNotification(false, false, -1, -2, false, false);
    }

    private void showReplyNotification() {
        showNotification(false, false, -1, 0, false, true);
    }

    /**
     *
     * @param needOnGoing 是否需要正在进行中
     * @param needDelete 是否需要监听清除消息
     * @param style 0:InboxStyle
     *              1:BigTextStyle
     *              2:BigPictureStyle
     *              3:MediaStyle
     * @param priority -2:NotificationCompat.PRIORITY_MIN
     *                 -1:PRIORITY_LOW
     *                 0:PRIORITY_DEFAULT
     *                 1:PRIORITY_HIGH
     *                 2:PRIORITY_MAX
     * @param needProgress 是否需要带进度条
     * @param hasReply 是否有带回复的样式
     */
    private void showNotification(boolean needOnGoing, boolean needDelete, int style, int priority, boolean needProgress, boolean hasReply) {
        number++;
        Intent intent = new Intent(this, NormalNotificationActivity.class);
        intent.putExtra("suoluo", "number:" + number);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, number, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("SL的通知标题")
                .setContentText("SL的通知内容，一般我都随便敲几个字，这次就认真敲一句话吧")
                .setTicker("消息来啦")
//                .setSubText("消息subtext")
                .setColor(Color.BLUE)
                .setNumber(number)
                .setAutoCancel(true)//点击后即移除
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.kg_listen_slide_menu_logout_btn_noraml)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.android))
                .setContentIntent(pendingIntent);

        if (needProgress) {
            builder.setContentTitle("我是有进度的通知--" + number*10 + "%");
            builder.setProgress(100, number * 10, false).setShowWhen(false);
        }

        // 通知是否需要展示在进行中
        if (needOnGoing) {
            builder.setContentTitle("我是正在进行中的通知");
            builder.setOngoing(true);//处于进行中的通知 有些系统在移除时会给提示(华为) 有些则移除不了(小米/三星)
        }

        // 是否需要监听清除消息的动作
        if (needDelete) {
            builder.setContentTitle("我是监听了清除事件的通知");
            Intent deleteIntent = new Intent(this, NormalNotificationActivity.class);
            deleteIntent.putExtra("suoluo", "delete:" + number);
            builder.setDeleteIntent(PendingIntent.getActivity(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }

        // 是否需要定义通知样式
        switch (style) {
            case -1:
                break;
            case 0:
                builder.setContentTitle("我是InboxStyle的通知");
                builder.setStyle(new NotificationCompat.InboxStyle().addLine("12345").addLine("678910").addLine("abcdefg"));
                break;
            case 1:
                builder.setContentTitle("我是BigTextStyle的通知");
                builder.setStyle(new NotificationCompat.BigTextStyle().setSummaryText("summaryText").setBigContentTitle("我是隐藏的通知标题").bigText("我是隐藏的通知内容，一般我都随便敲几个字，这次就认真敲一句话吧。我是隐藏的通知内容，一般我都随便敲几个字，这次就认真敲一句话吧"));
                break;
            case 2:
                builder.setContentTitle("我是BigPictureStyle的通知");
                builder.setStyle(new NotificationCompat.BigPictureStyle().setSummaryText("summaryText").setBigContentTitle("我是大图片模式").bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.notification_bigpic)));
                break;
            case 3:
                builder.setContentTitle("我是MediaStyle的通知");
                builder.addAction(R.drawable.kg_ic_playing_bar_pause_default, "", null);
                builder.addAction(R.drawable.kg_ic_playing_bar_next_default, "", null);
                builder.addAction(R.drawable.kg_ic_playing_bar_play_default, "", null);
                builder.setStyle(new NotificationCompat.MediaStyle().setMediaSession(
                        new MediaSessionCompat(this, "MediaSession",
                                new ComponentName(this, Intent.ACTION_MEDIA_BUTTON), null)
                                .getSessionToken()).setShowCancelButton(true).setShowActionsInCompactView(2));
                break;
            default:
                break;
        }

        if (priority != 0) {
            builder.setContentTitle("我是优先级为" + priority + "的通知");
            builder.setPriority(priority);
        }

        if (hasReply) {
            builder.setContentTitle("我是自带回复功能的通知标题");
            String replyLabel = "回复";
            RemoteInput remoteInput = new RemoteInput.Builder(NotificationBroadcastReceiver.KEY_REPLY)
                    .setLabel(replyLabel)
                    .build();

            NotificationCompat.Action action = new NotificationCompat.Action.Builder(0, replyLabel, getReplyPendingIntent())
                    .addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
                    .build();
            builder.addAction(action);
        }

//        Notification notification = builder.build();
//        mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        boolean enable = NotificationUtil.isNotificationEnabled(this);
        boolean apiEnable = NotificationManagerCompat.from(this).areNotificationsEnabled();
        if (enable || apiEnable) {
            Toast.makeText(this, "通知允许状态" , Toast.LENGTH_SHORT).show();
        } else {
            mBtn1.setText("通知禁止");
            Toast.makeText(this, "通知禁止状态" , Toast.LENGTH_SHORT).show();
        }
    }

    public PendingIntent getReplyPendingIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(this, NotificationBroadcastReceiver.class);
            intent.setAction(NotificationBroadcastReceiver.REPLY_ACTION);
            intent.putExtra(NotificationBroadcastReceiver.KEY_NOTIFICATION_ID, NOTIFICATION_ID);
            intent.putExtra(NotificationBroadcastReceiver.KEY_MESSAGE_ID, number);
            return PendingIntent.getBroadcast(this, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(this, CustomNotificationActivity.class);
            return PendingIntent.getActivity(this, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal1:
                showNormalNotification();
                break;
            case R.id.btn_normal2:
                showOnGongNotification();
                break;
            case R.id.btn_normal3:
                showNeedDeleteNotification();
                break;
            case R.id.btn_normal4:
                showStyleNotification();
                break;
            case R.id.btn_normal5:
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (number < 10) {
                            showProgressNotification();
                            handler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
                break;
            case R.id.btn_normal6:
                showMinPriorityNotification();
                break;
            case R.id.btn_normal7:
                showMaxPriorityNotification();
                break;
            case R.id.btn_normal8:
                showReplyNotification();
                break;
        }
    }
}

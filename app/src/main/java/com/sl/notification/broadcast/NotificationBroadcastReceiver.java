package com.sl.notification.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.widget.Toast;

import com.sl.notification.R;

/**
 * Created by wuhongqi on 17/1/17.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String REPLY_ACTION = "reply_action";
    public static String KEY_NOTIFICATION_ID = "key_notification_id";
    public static String KEY_MESSAGE_ID = "key_message_id";
    public static String KEY_REPLY = "reply";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            CharSequence message = getReplyMessage(intent);
            int messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);
            int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);
            Toast.makeText(context, "Message ID: " + messageId + "\nMessage: " + message,
                    Toast.LENGTH_SHORT).show();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.comm_ic_notification)
                    .setContentText("已回复");

            notificationManager.notify(notifyId, builder.build());
        }

    }

    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }
}

package com.example.englishapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.englishapp.R;
import com.example.englishapp.fragments.SettingsFragment;

public class NotiTimeReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
        //lấy NotificationID từ intent
        int notificationID = intent.getIntExtra("notificationID", 0);
        String msg = "Đến giờ học rồi";
        Intent mainIntent = new Intent(context,SettingsFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, mainIntent, 0);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Alarm Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        //khởi tạo notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("English App")
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(notificationID, builder.build());
    }
}

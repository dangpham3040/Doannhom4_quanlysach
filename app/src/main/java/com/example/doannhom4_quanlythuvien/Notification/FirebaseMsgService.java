package com.example.doannhom4_quanlythuvien.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.google.firebase.iid.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.doannhom4_quanlythuvien.ui.*;


public class FirebaseMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage rm) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        Log.d("msg", "onMessageReceived: " + rm.getNotification().getTitle());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
//                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(rm.getNotification().getTitle())
                .setContentText(rm.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }
}

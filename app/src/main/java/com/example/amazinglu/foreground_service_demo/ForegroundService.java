package com.example.amazinglu.foreground_service_demo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * foreground service must have a notification
 * */
public class ForegroundService extends Service {

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(MainActivity.STARTFOREGROUND_ACTION)) {
            /**
             * set up the intent for notification
             * */
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(MainActivity.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            /**
             * intent for notification back button
             * */
            Intent previousIntent = new Intent(this, ForegroundService.class);
            previousIntent.setAction(MainActivity.PREV_ACTION);
            PendingIntent pPreviousIntent = PendingIntent.getService(this, 0,
                    previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            /**
             * intent for notification play and pause button
             * */
            Intent playIntent = new Intent(this, ForegroundService.class);
            playIntent.setAction(MainActivity.PLAY_ACTION);
            PendingIntent pPlayIntent = PendingIntent.getService(this, 0,
                    playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            /**
             * intent for notification next button
             * */
            Intent nextIntent = new Intent(this, ForegroundService.class);
            nextIntent.setAction(MainActivity.NEXT_ACTION);
            PendingIntent pNextIntent = PendingIntent.getService(this, 0,
                    nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            /**
             * set up the notification
             * */
            Notification notification = new Notification.Builder(this, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_picture_in_picture_alt_black_18dp)
                    .setContentTitle("Foreground Service Demo")
                    .setContentText("fake music player")
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(new Notification.Action(R.drawable.baseline_fast_rewind_black_18dp,
                            "back", pPreviousIntent))
                    .addAction(new Notification.Action(R.drawable.baseline_play_arrow_black_18dp,
                            "play", pPlayIntent))
                    .addAction(new Notification.Action(R.drawable.baseline_fast_forward_black_18dp,
                            "back", pNextIntent))
                    .build();

            startForeground(MainActivity.NOTIFICATION_ID, notification);

        } else if (intent.getAction().equals(MainActivity.PREV_ACTION)) {
            Toast.makeText(this, "click prev", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(MainActivity.PLAY_ACTION)) {
            Toast.makeText(this, "click play button", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(MainActivity.NEXT_ACTION)) {
            Toast.makeText(this, "click next button", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(MainActivity.STOPFOREGROUND_ACTION)) {
            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

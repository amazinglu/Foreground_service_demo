package com.example.amazinglu.foreground_service_demo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class ForegroundServiceSeparateThread extends Service {

    private static final String KEY_INTENT_ACTION = "intent_action";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("Service_thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        Bundle args = new Bundle();
        args.putString(KEY_INTENT_ACTION, intent.getAction());
        msg.setData(args);
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    public Context getContext() {
        return this;
    }

    /**
     * inner handle class
     * */
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            // pass the looper to the handler
            super(looper);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            String action = msg.getData().getString(KEY_INTENT_ACTION);
            if (action.equals(MainActivity.STARTFOREGROUND_ACTION)) {
                /**
                 * set up the intent for notification
                 * */
                Intent notificationIntent = new Intent(getContext(), MainActivity.class);
                notificationIntent.setAction(MainActivity.MAIN_ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                /**
                 * intent for notification back button
                 * */
                Intent previousIntent = new Intent(getContext(), ForegroundServiceSeparateThread.class);
                previousIntent.setAction(MainActivity.PREV_ACTION);
                PendingIntent pPreviousIntent = PendingIntent.getService(getContext(), 0,
                        previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                /**
                 * intent for notification play and pause button
                 * */
                Intent playIntent = new Intent(getContext(), ForegroundServiceSeparateThread.class);
                playIntent.setAction(MainActivity.PLAY_ACTION);
                PendingIntent pPlayIntent = PendingIntent.getService(getContext(), 0,
                        playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                /**
                 * intent for notification next button
                 * */
                Intent nextIntent = new Intent(getContext(), ForegroundServiceSeparateThread.class);
                nextIntent.setAction(MainActivity.NEXT_ACTION);
                PendingIntent pNextIntent = PendingIntent.getService(getContext(), 0,
                        nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                /**
                 * set up the notification
                 * */
                Notification notification = new Notification.Builder(getContext(), MainActivity.CHANNEL_ID)
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

            } else if (action.equals(MainActivity.PREV_ACTION)) {
                Toast.makeText(getContext(), "click prev", Toast.LENGTH_LONG).show();
            } else if (action.equals(MainActivity.PLAY_ACTION)) {
                Toast.makeText(getContext(), "click play button", Toast.LENGTH_LONG).show();
            } else if (action.equals(MainActivity.NEXT_ACTION)) {
                Toast.makeText(getContext(), "click next button", Toast.LENGTH_LONG).show();
            } else if (action.equals(MainActivity.STOPFOREGROUND_ACTION)) {
                stopForeground(true);
                stopSelf();
            }
        }
    }
 }

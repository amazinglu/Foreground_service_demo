package com.example.amazinglu.foreground_service_demo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.start_service) Button startService;
    @BindView(R.id.stop_service) Button stopService;

    public static final String MAIN_ACTION = "com.example.amazinglu.foregroundservice.action.main";
    public static final String PREV_ACTION = "com.example.amazinglu.foregroundservice.action.prev";
    public static final String PLAY_ACTION = "com.example.amazinglu.foregroundservice.action.play";
    public static final String NEXT_ACTION = "com.example.amazinglu.foregroundservice.action.next";
    public static final String STARTFOREGROUND_ACTION = "com.example.amazinglu.foregroundservice.action.startforeground";
    public static final String STOPFOREGROUND_ACTION = "com.example.amazinglu.foregroundservice.action.stopforeground";
    public static final String CHANNEL_ID = "default_channel";
    public static final int NOTIFICATION_ID = 100;

    private NotificationChannel notificationChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (notificationChannel == null) {
            createNotificationChannel();
        }

        /**
         * to use the service, remember to register it to manifest
         * */
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this,
                        ForegroundServiceSeparateThread.class);
                startIntent.setAction(STARTFOREGROUND_ACTION);
                startService(startIntent);
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this,
                        ForegroundServiceSeparateThread.class);
                stopIntent.setAction(STOPFOREGROUND_ACTION);
                startService(stopIntent);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            // Register the notificationChannel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

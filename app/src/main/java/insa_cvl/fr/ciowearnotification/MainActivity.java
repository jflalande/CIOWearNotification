package insa_cvl.fr.ciowearnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat.WearableExtender;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sentNotification(View view) {

        // Regular notification
        // ====================
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Hey !")
                        .setContentText("You should check this information...");


        // Wearable specificities
        // ======================


        // Create an intent for the reply action
        // getBroadcast enables to have a broadcast message sent to the phone
        Intent actionIntent = new Intent("mynotification.intent");
        PendingIntent actionPendingIntent =
                PendingIntent.getBroadcast(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background,
                        "My action", actionPendingIntent)
                        .build();

        // Add action
        mBuilder.extend(new WearableExtender().addAction(action));


        // Fire the notification
        // =====================

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private BroadcastReceiver br_;

    @Override
    protected void onStart() {
        super.onStart();

        // Disable check box
        CheckBox cb = findViewById(R.id.checkBox);
        cb.setChecked(false); // Uncheck the box

        // Waiting for the PendingIntent from the wear app
        br_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("CIO", "PendingIntent received !");
                CheckBox cb = findViewById(R.id.checkBox);
                cb.setChecked(true); // Check the box !
            }
        };

        IntentFilter filter = new IntentFilter("mynotification.intent");
        this.registerReceiver(br_, filter);
        Log.i("CIO", "Filter installed.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(br_);
        Log.i("CIO", "Filter uninstalled.");
    }


}

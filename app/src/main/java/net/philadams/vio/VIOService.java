package net.philadams.vio;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * The right way to handle the server communication is likely with GCM;
 * for now we are doing it with AlarmManager and Retrofit
 */
public class VIOService extends Service {

  final String TAG = VIOService.class.getSimpleName();
  public static final int NOTIFICATION_ID = 1;
  Notification.Builder builder;
  NotificationManager notificationManager;
  PendingIntent alarmPendingIntent;
  ResultReceiver resultReceiver;
  AlarmManager alarmManager;

  @Override
  public void onCreate() {
    Log.d(TAG, "onStartCommand");

    // when the notification is tapped, launch the default VIOActivity
    Intent notificationTappedIntent = new Intent(this, VIOActivity.class);
    PendingIntent notificationTappedPendingIntent =
        PendingIntent.getActivity(this, 0, notificationTappedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    builder = new Notification.Builder(this).setSmallIcon(R.drawable.vio_fill_shape)
        .setContentTitle("Loading Anne's thoughts...")
        .setContentText("Is Anne thinking about you?")
        .addAction(R.drawable.vio_fill_shape, "I'm thinking about Anne", notificationTappedPendingIntent);

    builder.setContentIntent(notificationTappedPendingIntent);

    startForeground(NOTIFICATION_ID, builder.build());

    // set up notificationManager
    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    // set up resultReceiver
    resultReceiver = new ResultReceiver(new Handler()) {
      @Override
      protected void onReceiveResult(int resultCode, Bundle data) {
        Log.d(TAG, "result received!");
        builder.setContentTitle(data.getString("contentTitle"));
        builder.setContentText(data.getString("contentText"));
        updateVIONotification();
      }
    };

    // set up the AlarmManager for API polling
    setupAlarmManager();
  }

  @Override
  public void onDestroy() {
    cancelAlarm();
  }

  private void setupAlarmManager() {
    Log.d(TAG, "setupAlarmManager");
    Intent intent = new Intent(this, VIOAPIIntentService.class);
    intent.putExtra("resultReceiver", resultReceiver);
    alarmPendingIntent =
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_FIFTEEN_MINUTES,
        30000, alarmPendingIntent);  // this should be INTERVAL_FIFTEEN_MINUTES probably
  }

  private void cancelAlarm() {
    alarmManager.cancel(alarmPendingIntent);
  }

  public IBinder onBind(Intent intent) {
    return null;
  }

  public void updateVIONotification() {
    notificationManager.notify(NOTIFICATION_ID, builder.build());
    // stopping and starting the service works, but seems like overkill - notifying appears to work
    //stopForeground(true);
    //startForeground(NOTIFICATION_ID, builder.build());
  }
}

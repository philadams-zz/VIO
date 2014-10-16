package net.philadams.vio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 *
 */
public class VIOAPIService extends Service {

  private final String TAG = "APIService";

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand()");

    // TODO post 'thinking' to the vio api

    stopSelfResult(startId);
    return START_NOT_STICKY;
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate()");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy()");
  }

  public IBinder onBind(Intent intent) {
    return null;
  }

}

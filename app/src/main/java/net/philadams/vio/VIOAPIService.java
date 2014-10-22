package net.philadams.vio;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * TODO all the urls are ugly! fix them in semantic and restful ways
 */
public class VIOAPIService extends Service {

  private final String TAG = "APIService";
  private final int NOTIFICATION_ID = 1;
  private NotificationManager notificationManager;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand()");
    stopSelfResult(startId);
    return START_NOT_STICKY;
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate()");

    // TODO update widget outline? or do a notification?
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(this).setSmallIcon(R.drawable.vio_fill_shape)
            .setContentTitle("VIO")
            .setContentText("Sending 'thought' to Anne");
    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notificationManager.notify(NOTIFICATION_ID, builder.build());

    //outlineShape =
    //    (outlineShape == R.drawable.vio_outline_shape) ? R.drawable.vio_outline_shape_light
    //        : R.drawable.vio_outline_shape;
    //Log.d(TAG, String.format("alpha: %d", alpha));

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://vio.api.philadams.net")
        .setConverter(new GsonConverter(gson))
        .build();
    final VIOAPIInterface vio = restAdapter.create(VIOAPIInterface.class);

    // actually do POST 'thinking' to API
    new AsyncTask<Void, Void, Thought>() {
      @Override
      protected Thought doInBackground(Void... params) {
        Log.d(TAG, "posting thought to vio api...");
        Thought thought = vio.think(1, "Phil");
        Log.d(TAG, String.format("new Thought: %s", thought.toString()));
        return thought;
      }
    }.execute();
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy()");
    //notificationManager.cancel(NOTIFICATION_ID);  // TODO cancel after some time?
  }

  public IBinder onBind(Intent intent) {
    return null;
  }
}

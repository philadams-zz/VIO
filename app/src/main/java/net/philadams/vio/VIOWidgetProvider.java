package net.philadams.vio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 *
 */
public class VIOWidgetProvider extends AppWidgetProvider {

  final String TAG = "VIOWidgetProvider";
  private int outlineShape = R.drawable.vio_outline_shape;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    ComponentName thisWidget = new ComponentName(context, VIOWidgetProvider.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    for (int widgetId : allWidgetIds) {

      RemoteViews remoteViews =
          new RemoteViews(context.getPackageName(), R.layout.vio_widget_layout);
      remoteViews.setInt(R.id.vio_widget_fill, "setImageAlpha", getAlpha());
      remoteViews.setInt(R.id.vio_widget_outline, "setImageResource", outlineShape);

      // pending intent for widget onclick that posts a 'thinking' via the VIOAPIService
      Intent intent = new Intent(context, VIOAPIService.class);
      PendingIntent pendingIntent =
          PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);

      // update widget
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
  }

  private int getAlpha() {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://vio.api.philadams.net")
        .setConverter(new GsonConverter(gson))
        .build();
    final VIOAPIInterface vio = restAdapter.create(VIOAPIInterface.class);

    // grab thoughts list from vio api
    AsyncTask<Void, Void, List<Thought>> myTask = new AsyncTask<Void, Void, List<Thought>>() {
      @Override
      protected List<Thought> doInBackground(Void... params) {
        Log.d(TAG, "grabbing list of thoughts from vio api...");
        List<Thought> thoughtList = vio.thoughts(1);
        for (Thought thought : thoughtList) {
          Log.d(TAG, String.format("Thought: %s", thought.toString()));
        }
        return thoughtList;
      }
    }.execute();
    try {
      List<Thought> thoughts = myTask.get();
      // TODO return some repr of recent thought.when relative to time.now
      return (new Random().nextInt(255));
    } catch (InterruptedException e) {
      e.printStackTrace();
      return (new Random().nextInt(255));
    } catch (ExecutionException e) {
      e.printStackTrace();
      return (new Random().nextInt(255));
    }
  }
}

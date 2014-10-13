package net.philadams.vio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Random;

/**
 * Created by phil on 10/13/14.
 */
public class VIOWidgetProvider extends AppWidgetProvider {

  final String TAG = "VIOWidgetProvider";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    ComponentName thisWidget = new ComponentName(context, VIOWidgetProvider.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    for (int widgetId : allWidgetIds) {
      int number = (new Random().nextInt(100));

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.vio_widget_layout);
      Log.d(TAG, String.format("random number: %d", number));
      remoteViews.setTextViewText(R.id.update, String.valueOf(number));

      Intent intent = new Intent(context, VIOWidgetProvider.class);
      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

      PendingIntent pendingIntent =
          PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

  }

}

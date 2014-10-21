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

      int alpha = getAlpha();
      outlineShape =
          (outlineShape == R.drawable.vio_outline_shape) ? R.drawable.vio_outline_shape_light
              : R.drawable.vio_outline_shape;
      Log.d(TAG, String.format("alpha: %d", alpha));

      RemoteViews remoteViews =
          new RemoteViews(context.getPackageName(), R.layout.vio_widget_layout);
      remoteViews.setInt(R.id.vio_widget_fill, "setImageAlpha", alpha);
      remoteViews.setInt(R.id.vio_widget_outline, "setImageResource", outlineShape);
      //remoteViews.setTextViewText(R.id.update, String.valueOf(number));

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
    return (new Random().nextInt(255));  // this value will really be set from the API
  }
}

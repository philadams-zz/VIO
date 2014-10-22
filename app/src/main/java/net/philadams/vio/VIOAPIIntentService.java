package net.philadams.vio;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Random;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 *
 */
public class VIOAPIIntentService extends IntentService {

  private final String TAG = VIOAPIIntentService.class.getSimpleName();
  private int coupleId = 1;  // TODO grab from prefs
  private int resultCode = 1;

  /**
   * A constructor is required, and must call the super IntentService(String)
   * constructor with a name for the worker thread.
   */
  public VIOAPIIntentService() {
    super("VIOAPIIntentService");
  }

  /**
   * The IntentService calls this method from the default worker thread with
   * the intent that started the service. When this method returns, IntentService
   * stops the service, as appropriate.
   */
  @Override
  public void onHandleIntent(Intent intent) {
    Log.d(TAG, "hello from the VIO API IntentService!");

    // hit up the API
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://vio.api.philadams.net")
        .setConverter(new GsonConverter(gson))
        .build();
    final VIOAPIInterface vio = restAdapter.create(VIOAPIInterface.class);
    List<Thought> thoughtList = vio.thoughts(coupleId);

    // TODO from thought list, grab most recent time 'other' thought of this user
    for (Thought thought : thoughtList) {
      Log.d(TAG, String.format("Thought: %s", thought.toString()));
    }

    // TODO convert timeSince to some friendly human metric ('within the hour', 'hasn't used VIO yet', etc.)
    int minsAgo = (new Random()).nextInt(60);

    // build bundle from results
    Bundle resultsBundle = new Bundle();
    resultsBundle.putString("contentTitle", "Anne is thinking about you");
    resultsBundle.putString("contentText",
        String.format("As of about %d minutes ago", minsAgo));

    // return data to VIOService
    ResultReceiver resultReceiver = intent.getParcelableExtra("resultReceiver");
    resultReceiver.send(resultCode, resultsBundle);
  }

}

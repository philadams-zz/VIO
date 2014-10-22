package net.philadams.vio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * TODO: show list of recent thoughts?
 */
public class VIOActivity extends Activity {

  private final String TAG = VIOActivity.class.getSimpleName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // start the long-running VIOService
    Intent vioServiceIntent = new Intent(this, VIOService.class);
    startService(vioServiceIntent);

    // TODO allow user to stop the voiservice with a button in this activity...
    // that will have to call (1) ACTION_BACKGROUND intent to the service and then
    // (2) stopService(stopIntent)
  }
}

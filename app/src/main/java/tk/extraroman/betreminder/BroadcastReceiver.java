package tk.extraroman.betreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by ducba on 2016/04/05.
 */
public class BroadcastReceiver extends WakefulBroadcastReceiver {
    public static String ACTION_ALARM = "tk.extraroman.alaram";
    public static String MESSENGER = "tk.extraroman.messenger";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = bundle.getString(ACTION_ALARM);
        Messenger messenger = (Messenger) bundle.get(MESSENGER);
        if (action.equals(ACTION_ALARM)) {
            Intent inService = new Intent(context, GetBetTimeService.class);
            inService.putExtra(MESSENGER, messenger);
            context.startService(inService);
        }
    }
}

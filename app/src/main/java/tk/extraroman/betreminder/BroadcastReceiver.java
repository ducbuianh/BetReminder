package tk.extraroman.betreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by ducba on 2016/04/05.
 */
public class BroadcastReceiver extends WakefulBroadcastReceiver {
    public static String ACTION_ALARM = "com.alarammanager.alaram";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = bundle.getString(ACTION_ALARM);
        if (action.equals(ACTION_ALARM)) {
            Toast.makeText(context, "Entered", Toast.LENGTH_SHORT).show();
            Intent inService = new Intent(context, GetBetTimeService.class);
            context.startService(inService);
        }
        else
        {
            Toast.makeText(context, "Else loop", Toast.LENGTH_SHORT).show();
        }
    }
}

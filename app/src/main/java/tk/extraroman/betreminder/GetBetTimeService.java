package tk.extraroman.betreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by ducba on 2016/04/04.
 */
public class GetBetTimeService extends IntentService {
    //private String time;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public GetBetTimeService(String name) {
        super("tk.extraroman.GetBetTimeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();

        try {
            Document doc = Jsoup.connect(dataString).timeout(30000).get();
            Element bets = doc.getElementById("bets");
            Element timeElem = bets.child(0).child(0).child(0);
            //this.time = timeElem.text();
            sendNotification(timeElem.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String msg)
    {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setContentTitle("Bet Now !!!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent).setContentTitle("").setContentText("");
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

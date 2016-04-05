package tk.extraroman.betreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GetBetTimeService extends IntentService {
    //private String time;
    private static final int TIME_THRESHOLD = 15;
    private static final String TIME_UNIT = "hour";
    private static final String PARSE_URL = "https://csgolounge.com/";
    private static final int PARSE_TIMEOUT = 30*1000;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public GetBetTimeService() {
        super("GetBetTimeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        try {
            Document doc = Jsoup.connect(PARSE_URL).timeout(PARSE_TIMEOUT).get();
            Element bets = doc.getElementById("bets");
            Element timeElem = bets.child(0).child(0).child(0);
            if (isTimeToBet(timeElem.text())) {
                Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
                sendNotification("Bet now u lazy ass !!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String msg)
    {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

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

    private boolean isTimeToBet(String time) {
        String[] timeElem = time.split(" ");
        System.out.println("asdfasdfasd: " + timeElem[0] + " " + timeElem[1]);
        return (Integer.parseInt(timeElem[0]) <= TIME_THRESHOLD && TIME_UNIT.equals(timeElem[1].substring(0, 4)));
    }
}

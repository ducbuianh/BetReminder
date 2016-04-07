package tk.extraroman.betreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetBetTimeService extends IntentService {
    //private String time;
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
            Document doc = Jsoup.connect(Constants.PARSE_URL).timeout(PARSE_TIMEOUT).get();
            //Element bets = doc.getElementById("bets");
            //Element timeElem = bets.child(0).child(0).child(0);

            Element divTables = doc.select("div.tables").first();
            Elements events = divTables.select("tr.event");

            long matchTimeStamp = 0;
            String matchName = "";
            String matchTime = "";

            for (Element event : events) {
                Element eventStatus = event.select("td.event-status").first();
                if (StringUtil.isBlank(eventStatus.text())) {
                    Element eventTimeStamp = event.select("td.event-time > a > span.phpunixtime").first();
                    matchTimeStamp = Long.parseLong(eventTimeStamp.text());
                    Element eventTime = event.select("td.event-time > a ").first();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    matchTime = format.format(new Date(matchTimeStamp));
                    matchName = eventTime.attr("title");
                    break;
                }
            }

            if (isTimeToBet(matchTimeStamp)) {
                sendNotification("[" + matchName + "] will start at " + matchTime);
            }
        } catch (UnknownHostException ex) {
            Log.e("debug", "Unknown host exception occurred");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String msg)
    {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentTitle("Bet Reminder")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        mBuilder.setTicker("We have a new bet coming");
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private boolean isTimeToBet(long nextMatchTime) {
        return (System.currentTimeMillis() + calculateThresholdTime() >= nextMatchTime);
    }

    private long calculateThresholdTime() {
        int thresholdValue = Integer.parseInt(PreferenceUtil.getPreference(getApplicationContext(), Constants.THRESHOLD_VALUE_KEY));
        char thresholdUnit = PreferenceUtil.getPreference(getApplicationContext(), Constants.THRESHOLD_UNIT_KEY).charAt(0);

        if (thresholdUnit == 'h') {
            return thresholdValue*3600*1000;
        } else {
            return thresholdValue*60*1000;
        }
    }
}

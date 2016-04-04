package tk.extraroman.betreminder;

import android.app.IntentService;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by ducba on 2016/04/04.
 */
public class GetBetTimeService extends IntentService {
    private String time;

    public GetBetTimeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        try {
            Document doc = Jsoup.connect(dataString).timeout(30000).get();
            Element bets = doc.getElementById("bets");
            Element timeElem = bets.child(0).child(0).child(0);
            this.time = timeElem.text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Do work here, based on the contents of dataString
    }
}

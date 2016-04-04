package tk.extraroman.betreminder;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by ducba on 2016/04/04.
 */
public class GetBetTimeTask extends AsyncTask<String, Integer, String> {
    private String time;
    private WeakReference<MainActivity> mActivity;
    public GetBetTimeTask(MainActivity activity) {
        mActivity = new WeakReference<MainActivity>(activity);
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            Document doc = Jsoup.connect(urls[0]).timeout(30000).get();
            Element bets = doc.getElementById("bets");
            Element timeElem = bets.child(0).child(0).child(0);
            this.time = timeElem.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        MainActivity activity = mActivity.get();
        if (activity != null) {
            TextView text = (TextView) activity.findViewById(R.id.abc);
            text.setText(this.time);
        }
    }
}

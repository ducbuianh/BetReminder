package tk.extraroman.betreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ducba on 2016/04/06.
 */
public class PreferenceUtil extends AppCompatActivity {
    public static final String PREFS_NAME = "BetReminder Preference";

    public static void savePreference(Context ctxt, String key, String value) {
        SharedPreferences settings = ctxt.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreference(Context ctxt, String key) {
        SharedPreferences settings = ctxt.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, "");
    }
}

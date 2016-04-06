package tk.extraroman.betreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Spinner thresholdValueSpinner = (Spinner) findViewById(R.id.threshold_value_spinner);
        ArrayAdapter<CharSequence> thresholdValueAdapter = ArrayAdapter.createFromResource(this,
                R.array.bet_threshold_minute_value_array, android.R.layout.simple_spinner_item);
        thresholdValueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thresholdValueSpinner.setAdapter(thresholdValueAdapter);
        thresholdValueSpinner.setSelection(0);

        thresholdValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceUtil.savePreference(getApplicationContext(), Constants.THRESHOLD_VALUE_KEY, parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner thresholdUnitSpinner = (Spinner) findViewById(R.id.threshold_unit_spinner);
        ArrayAdapter<CharSequence> thresholdUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.bet_threshold_unit_array, android.R.layout.simple_spinner_item);
        thresholdUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thresholdUnitSpinner.setAdapter(thresholdUnitAdapter);
        thresholdUnitSpinner.setSelection(0);

        thresholdUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceUtil.savePreference(parent.getContext(), Constants.THRESHOLD_UNIT_KEY, parent.getSelectedItem().toString());

                int valueArrayId = 0;
                if (parent.getSelectedItem().toString().charAt(0) == 'h') {
                    valueArrayId = R.array.bet_threshold_hour_value_array;
                } else {
                    valueArrayId = R.array.bet_threshold_minute_value_array;
                }
                ArrayAdapter<CharSequence> newAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                        valueArrayId, android.R.layout.simple_spinner_item);
                thresholdValueSpinner.setAdapter(newAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner checkIntervalSpinner = (Spinner) findViewById(R.id.bet_check_interval_spinner);
        ArrayAdapter<CharSequence> checkIntervalAdapter = ArrayAdapter.createFromResource(this,
                R.array.bet_check_interval_array, android.R.layout.simple_spinner_item);
        checkIntervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkIntervalSpinner.setAdapter(checkIntervalAdapter);
        checkIntervalSpinner.setSelection(0);

        checkIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceUtil.savePreference(parent.getContext(), Constants.CHECK_INTERVAL_KEY, parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button btnStop = (Button) findViewById(R.id.buttonStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlarmManager alarms = (AlarmManager) v.getContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                    Intent intent = new Intent(v.getContext(), BroadcastReceiver.class);
                    PendingIntent pIntent = PendingIntent.getBroadcast(v.getContext(),
                            1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarms.cancel(pIntent);

                    Button btnStart = (Button) findViewById(R.id.buttonStart);
                    btnStart.setEnabled(true);
                    Spinner spinner1 = (Spinner) findViewById(R.id.threshold_unit_spinner);
                    Spinner spinner2 = (Spinner) findViewById(R.id.threshold_value_spinner);
                    Spinner spinner3 = (Spinner) findViewById(R.id.bet_check_interval_spinner);
                    spinner1.setEnabled(true);
                    spinner2.setEnabled(true);
                    spinner3.setEnabled(true);
                    v.setEnabled(false);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btnStop.setEnabled(false);

        final Button btnStart = (Button) findViewById(R.id.buttonStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlarmManager alarms = (AlarmManager) v.getContext().getSystemService(getApplicationContext().ALARM_SERVICE);

                    Intent intent = new Intent(v.getContext(), BroadcastReceiver.class);
                    intent.putExtra(BroadcastReceiver.ACTION_ALARM, BroadcastReceiver.ACTION_ALARM);

                    PendingIntent pIntent = PendingIntent.getBroadcast(v.getContext(),
                            1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    int interval = Integer.parseInt(PreferenceUtil.getPreference(v.getContext(), Constants.CHECK_INTERVAL_KEY).split(" ")[0]);
                    alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis(), interval*60*1000, pIntent);

                    Button btnStop = (Button) findViewById(R.id.buttonStop);
                    btnStop.setEnabled(true);
                    Spinner spinner1 = (Spinner) findViewById(R.id.threshold_unit_spinner);
                    Spinner spinner2 = (Spinner) findViewById(R.id.threshold_value_spinner);
                    Spinner spinner3 = (Spinner) findViewById(R.id.bet_check_interval_spinner);
                    spinner1.setEnabled(false);
                    spinner2.setEnabled(false);
                    spinner3.setEnabled(false);
                    v.setEnabled(false);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            this.finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }
}

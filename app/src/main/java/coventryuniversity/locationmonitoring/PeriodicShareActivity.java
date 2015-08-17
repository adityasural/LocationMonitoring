package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PeriodicShareActivity extends Activity{

    public static final String TAG = MapsActivity.class.getSimpleName();
    private String messageToBeSent;

    //AlarmManager related
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);

        DatabaseHandler db = new DatabaseHandler(this);
        List<String> names=db.getUserNameList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner userNameSpinner=(Spinner) findViewById(R.id.periodicUserSpinner);
        userNameSpinner.setAdapter(dataAdapter);

//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
//                new Intent(this, AlarmReceiver.class),
//                PendingIntent.FLAG_NO_CREATE) != null);
//        if(alarmUp){
//            TextView alarmSet=(TextView)findViewById(R.id.alarmSet);
//            alarmSet.setText("The alarm has already been set");
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    public void startAlarm(View view) {

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        final Spinner userNameSpinner=(Spinner) findViewById(R.id.periodicUserSpinner);
        String name=userNameSpinner.getSelectedItem().toString();

        DatabaseHandler db = new DatabaseHandler(this);
        UserDetails userDetails=db.getUserDetails(name);



        alarmIntent.putExtra("phoneNumber", userDetails.PhoneNumber);
        alarmIntent.putExtra("name",userDetails.Name);

        // Retrieve a PendingIntent that will perform a broadcast
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        Spinner secondsSpinner=(Spinner) findViewById(R.id.secondSpinner);
        int intervalFromSpinner= Integer.parseInt(secondsSpinner.getSelectedItem().toString());

        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = intervalFromSpinner*1000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Started sharing location", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(View view) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Stopped sharing location", Toast.LENGTH_SHORT).show();


    }


}

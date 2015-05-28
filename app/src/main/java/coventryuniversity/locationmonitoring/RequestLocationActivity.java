package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;


public class RequestLocationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_location);

        DatabaseHandler db = new DatabaseHandler(this);
        List<String> names=db.getUserNameList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner userNameSpinner=(Spinner) findViewById(R.id.requestUserSpinner);
        userNameSpinner.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_location, menu);
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

    public void requestLocation(View view){

        final Spinner userNameSpinner=(Spinner) findViewById(R.id.requestUserSpinner);
        String name=userNameSpinner.getSelectedItem().toString();

        DatabaseHandler db = new DatabaseHandler(this);
        UserDetails userDetails=db.getUserDetails(name);

        String smsBody = "getlocation";

        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        // Send a text based SMS
        smsManager.sendTextMessage(userDetails.PhoneNumber, null, smsBody, null, null);

        Toast.makeText(this,"Location of "+userDetails.Name+" requested",Toast.LENGTH_SHORT).show();

    }
}

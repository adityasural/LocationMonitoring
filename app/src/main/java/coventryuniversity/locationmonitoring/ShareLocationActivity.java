package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ShareLocationActivity extends Activity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    String messageToBeSent="neverland";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);

        DatabaseHandler db = new DatabaseHandler(this);
        List<String> names=db.getUserNameList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner userNameSpinner=(Spinner) findViewById(R.id.shareUserSpinner);
        userNameSpinner.setAdapter(dataAdapter);

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public void shareMyLocation(View view){

        final Spinner userNameSpinner=(Spinner) findViewById(R.id.shareUserSpinner);
        String name=userNameSpinner.getSelectedItem().toString();

        DatabaseHandler db = new DatabaseHandler(this);
        UserDetails userDetails=db.getUserDetails(name);

        if(userDetails.isEligible){
            String smsBody = "I am at "+messageToBeSent;

            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send a text based SMS
            smsManager.sendTextMessage(userDetails.PhoneNumber, null, smsBody, null, null);
            Toast.makeText(this, "Location shared with " + userDetails.Name, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,userDetails.Name+" not eligible",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_location, menu);
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

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        TextView myLocationTextView=(TextView)findViewById(R.id.myLocationText);

        if (mLastLocation != null) {
            double latitude=mLastLocation.getLatitude();
            double longitude=mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                if(addresses!=null && addresses.size() > 0)
                {
                    messageToBeSent=addresses.get(0).getAddressLine(0)+", "+
                            addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2);
                    myLocationTextView.setText(messageToBeSent);
                }
            } catch (IOException e) {
                String TAG="";
                Log.e(TAG, e.getMessage());
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

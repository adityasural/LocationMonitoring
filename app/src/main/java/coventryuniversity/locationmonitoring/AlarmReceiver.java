package coventryuniversity.locationmonitoring;

/**
 * Created by VPAPSA on 25-05-2015.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    String TAG="AlarmReceiver";
    private String messageToBeSent;
    private GoogleApiClient mGoogleApiClient;
    private Context alarmReceiverContext;
    private String phoneNumber="";
    private String name="";

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        alarmReceiverContext=context;

        phoneNumber=intent.getStringExtra("phoneNumber");
        name=intent.getStringExtra("name");

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude=mLastLocation.getLatitude();
            double longitude=mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(alarmReceiverContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                if(addresses!=null && addresses.size() > 0)
                {
                    messageToBeSent=addresses.get(0).getAddressLine(0)+", "+
                            addresses.get(0).getAddressLine(1);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            String smsBody = "I am at "+messageToBeSent;

            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send a text based SMS
            smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);

            Toast.makeText(alarmReceiverContext, "Location shared with "+name, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

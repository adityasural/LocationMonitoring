package coventryuniversity.locationmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private String TAG = SmsReceiver.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String messageToBeSent;
    private String requestor;
    private Context smsReceiverContext;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        smsReceiverContext=context;
        SmsMessage[] msgs = null;
        String messageBody="";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Fetch the text message
                messageBody= msgs[i].getMessageBody().toString();
                requestor=msgs[i].getOriginatingAddress();
            }

            DatabaseHandler db=new DatabaseHandler(context);
            List<UserDetails> users=db.getUserDetailsList();
            boolean userIsEligible=false;
            for(UserDetails ud:users){
                if(requestor.contains(ud.PhoneNumber) && ud.isEligible){
                    userIsEligible=true;
                    break;
                }
            }

            if(messageBody!="" && messageBody.toLowerCase().contains("getlocation") && userIsEligible){
                // Display the entire SMS Message
                Log.d(TAG, messageBody);
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                mGoogleApiClient.connect();
            }
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude=mLastLocation.getLatitude();
            double longitude=mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(smsReceiverContext, Locale.getDefault());
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
            String phoneNumber = requestor;
            String smsBody = "I am at "+messageToBeSent;

            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send a text based SMS
            smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

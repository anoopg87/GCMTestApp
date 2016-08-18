package com.android.app.gcmtestapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;


/// use http://apns-gcm.bryantan.info/  to test GCM
public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private BroadcastReceiver mGcmRegisterReceiver;



    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mGcmRegisterReceiver,new IntentFilter(GcmRegisterIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mGcmRegisterReceiver,new IntentFilter(GcmRegisterIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGcmRegisterReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGcmRegisterReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(GcmRegisterIntentService.REGISTRATION_SUCCESS)){

                    Toast.makeText(getApplicationContext(),intent.getStringExtra("token"),Toast.LENGTH_LONG).show();

                }else if (intent.getAction().equals(GcmRegisterIntentService.REGISTRATION_ERROR)){

                    Toast.makeText(getApplicationContext(),"Error in gcm registration",Toast.LENGTH_LONG).show();

                }

            }
        };

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

        }else{
            Intent intent=new Intent(this,GcmRegisterIntentService.class);
            startService(intent);
        }

    }
}

package com.profilohn.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.profilohn.BuildConfig;
import com.profilohn.R;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (BuildConfig.DEBUG) {
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            RequestConfiguration configuration =
                    new RequestConfiguration.Builder()
                            .setTestDeviceIds(Arrays.asList(
                                    android_id,
                                    AdRequest.DEVICE_ID_EMULATOR)
                            ).build();

            MobileAds.setRequestConfiguration(configuration);
            Log.d("Android","Android ID : " + android_id);
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        _timeOut();
    }


    /**
     * Initializes time out depending
     * redirect to home activity.
     */
    private void _timeOut()
    {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HelloActivity.class);
                startActivity(i);

                finish();
            }
        }, getResources().getInteger(R.integer.splash_timeout));
    }

}

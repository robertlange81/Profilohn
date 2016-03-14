package sageone.abacus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import sageone.abacus.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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

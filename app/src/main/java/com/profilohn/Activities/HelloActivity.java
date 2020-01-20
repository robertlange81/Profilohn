package com.profilohn.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.util.Calendar;
import com.profilohn.Helper.ConnectivityHandler;
import com.profilohn.Helper.MessageHelper;
import com.profilohn.Helper.SystemHelper;
import com.profilohn.Helper.FileStore;
import com.profilohn.Interfaces.ApiCallbackListener;
import com.profilohn.Models.Calculation;
import com.profilohn.Models.Insurances;
import com.profilohn.Models.WebService;
import com.profilohn.R;

public class HelloActivity extends AppCompatActivity implements ApiCallbackListener {

    private static final Integer CALC_TYPE_NET = 0;
    private static final Integer CALC_TYPE_GROSS = 1;

    private FileStore fileStore;
    private Dialog preparationDialog;
    private WebService webService;
    private ConnectivityHandler connectivityHandler;

    public static HelloActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);

        /*
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("Android","errorCode : " + errorCode);
            }
        });
        */

        _registerConnectivityReceiver();

        Button startCalcNet = (Button) findViewById(R.id.hello_start_calculation_net);
        Button startCalcGross = (Button) findViewById(R.id.hello_start_calculation_gross);

        if(startCalcNet != null)
            startCalcNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputActivity(CALC_TYPE_NET);
                }
            });

        if(startCalcGross != null)
            startCalcGross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputActivity(CALC_TYPE_GROSS);
                }
            });

        TextView t = (TextView) findViewById(R.id.hello_credits);
        if(t != null)
            t.setText(t.getText().toString().replace("YYYY", Calendar.getInstance().get(Calendar.YEAR) + ""));

        instance = this;
        fileStore = new FileStore(this);

        if(connectivityHandler != null) {
            WebService.host = connectivityHandler.getHost();
        }

        webService = new WebService(getApplicationContext(), this);
        _prefetchInsurances();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Register some receiver and
     * handle connectivity changes.
     */
    private void _registerConnectivityReceiver()
    {
        connectivityHandler = new ConnectivityHandler(this);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(
                connectivityHandler,
                intentFilter
        );
    }

    public void showInputActivity(Integer type)
    {
        Intent i = new Intent(this, InputActivity.class);
        i.putExtra("calc_type", type);
        startActivity(i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
            break;
            case R.id.action_cache:
                fileStore.flush();
                _prefetchInsurances();
                break;
            case R.id.action_quit:
                SystemHelper.finish(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Fetch insurances from API
     * and sav them to app cache.
     */
    public void _prefetchInsurances()
    {
        try {
            fileStore.readInsurancesResult();
            return;
        } catch (FileNotFoundException e) {
            // fetch insurances ..
            String x = e.getMessage();
        }

        dialog(getResources().getString(R.string.preparation_dialog), true);

        webService.Insurances();
    }


    @Override
    public void responseFinishInsurances(Insurances i)
    {
        dismissDialog();
        fileStore.writeInsurancesResult(i);
        MessageHelper.snackbar(this, "Krankenkassendaten wurden aktualisiert");
    }

    @Override
    public void responseFailedInsurances(String message)
    {
        dismissDialog();
        // dialog(message, false);
        MessageHelper.snackbar(this, "Fehler beim Aktualisieren der Krankenkassen: " + message);
    }

    @Override
    public void responseFinishCalculation(Calculation calculation) {

    }

    @Override
    public void responseFailedCalculation(String message) {

    }


    private void dialog(String message, boolean modal)
    {
        Dialog d = MessageHelper.dialog(this, modal, message);
        d.setCancelable(false);
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                String x = "";
            }
        });
        d.show();

        preparationDialog = d;
    }


    private void dismissDialog()
    {
        preparationDialog.dismiss();
    }

}

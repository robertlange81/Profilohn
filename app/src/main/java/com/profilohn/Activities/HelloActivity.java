package com.profilohn.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private ConnectivityHandler connectivityHandler;
    private FileStore fileStore;
    private Dialog preparationDialog;
    private WebService webService;

    public static HelloActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);
        _registerConnectivityReceiver();

        Button startCalcNet = (Button) findViewById(R.id.hello_start_calculation_net);
        Button startCalcGross = (Button) findViewById(R.id.hello_start_calculation_gross);

        startCalcNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputActivity(CALC_TYPE_NET);
            }
        });

        startCalcGross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputActivity(CALC_TYPE_GROSS);
            }
        });

        TextView t = (TextView) findViewById(R.id.hello_credits);
        t.setText(t.getText().toString().replace("YYYY", Calendar.getInstance().get(Calendar.YEAR) + ""));

        instance = this;
        fileStore = new FileStore(this);

        webService = new WebService(getApplicationContext(), this);
        _prefetchInsurances();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        InputActivity.isCalculationEnabled = true;
    }


    /**
     * Inflate the menu.
     * This adds items to the action bar if it is present.
     *
     * @param menu
     * @return
     */
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


    /**
     * Select and display the
     * calculation input activity.
     *
     * @param type
     */
    public void showInputActivity(Integer type)
    {
        Intent i = new Intent(this, InputActivity.class);
        i.putExtra("calc_type", type);
        startActivity(i);
    }


    @Override
    /**
     * Main menu selection callback handling.
     */
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
            Insurances i = fileStore.readInsurancesResult();
            return;
        } catch (FileNotFoundException e) {
            // fetch insurances ..
        }

        dialog(getResources().getString(R.string.preparation_dialog), true);

        webService.Insurances();
    }


    @Override
    /**
     *  Callback for insurances api call.
     */
    public void responseFinishInsurances(Insurances i)
    {
        dismissDialog();
        fileStore.writeInsurancesResult(i);
    }

    @Override
    public void responseFailedInsurances(String message)
    {
        dismissDialog();
        dialog(message, false);
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
        d.show();

        preparationDialog = d;
    }


    private void dismissDialog()
    {
        preparationDialog.dismiss();
    }

}

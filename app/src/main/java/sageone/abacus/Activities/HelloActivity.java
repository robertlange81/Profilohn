package sageone.abacus.Activities;

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

import java.io.IOException;
import java.util.Calendar;

import sageone.abacus.Exceptions.StatusCodeException;
import sageone.abacus.Helper.ConnectivityHandler;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Helper.SystemHelper;
import sageone.abacus.Helper.FileStore;
import sageone.abacus.Interfaces.ApiCallbackListener;
import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.Insurances;
import sageone.abacus.Models.WebService;
import sageone.abacus.R;

public class HelloActivity extends AppCompatActivity implements ApiCallbackListener {

    private static final Integer CALC_TYPE_NETTO  = 0;
    private static final Integer CALC_TYPE_BRUTTO = 1;

    private ConnectivityHandler connectivityHandler;
    private FileStore fileStore;
    private Dialog preparationDialog;

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
                showInputActivity(CALC_TYPE_NETTO);
            }
        });

        startCalcGross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputActivity(CALC_TYPE_BRUTTO);
            }
        });

        TextView t = (TextView) findViewById(R.id.hello_credits);
        t.setText(t.getText().toString().replace("YYYY", Calendar.getInstance().get(Calendar.YEAR) + ""));

        instance = this;
        fileStore = new FileStore(this);

        _prefetchInsurances();
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
     * handle connectivity changements.
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
        } catch (IOException e) {
            // no cache here. fetch them from the api ..
        }

        preparationDialog = MessageHelper.dialog(this, true,
                getResources().getString(R.string.preparation_dialog));
        preparationDialog.show();

        WebService webService = WebService.getInstance(getApplicationContext(), this);
        try {
            webService.Insurances();
        } catch (StatusCodeException e) {
        }
    }


    @Override
    /**
     *  Callback for insurances api call.
     */
    public void responseFinishInsurances(Insurances i)
    {
        fileStore.writeInsurancesResult(i);
        preparationDialog.dismiss();
    }

    @Override
    public void responseFailedInsurances(String message)
    {
        preparationDialog.dismiss();
        MessageHelper.dialog(this, false, getString(R.string.exception_status_code) + "\n\n Details:\n" + message);
    }

    @Override
    public void responseFinishCalculation(Calculation calculation) {

    }

    @Override
    public void responseFailedCalculation(String message) {

    }

}

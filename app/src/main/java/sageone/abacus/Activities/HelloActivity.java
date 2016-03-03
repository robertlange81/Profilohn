package sageone.abacus.Activities;

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

import java.util.Calendar;

import sageone.abacus.Helper.ConnectivityHandler;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Helper.SystemHelper;
import sageone.abacus.Helper.FileStore;
import sageone.abacus.R;

public class HelloActivity extends AppCompatActivity {

    private static final Integer CALC_TYPE_NETTO  = 0;
    private static final Integer CALC_TYPE_BRUTTO = 1;

    private ConnectivityHandler connectivityHandler;
    private FileStore fileStore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hello);
        _registerConnectivityReceiver();

        // Delete the result object cache
        fileStore = new FileStore(this);
        fileStore.delete();

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
        return true;
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
                fileStore.delete();
                MessageHelper.dialog(this, false
                        , getResources().getString(R.string.hello_cache_cleared)
                        , MessageHelper.DIALOG_TYPE_INFO).show();
                break;
            case R.id.action_quit:
                SystemHelper.finish(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

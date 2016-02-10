package sageone.abacus.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import sageone.abacus.Helper.ConnectivityHandler;
import sageone.abacus.Helper.SystemHelper;
import sageone.abacus.R;

public class HelloActivity extends AppCompatActivity {

    private static final Integer CALC_TYPE_NETTO  = 0;
    private static final Integer CALC_TYPE_BRUTTO = 1;

    private FloatingActionButton fab;
    private ConnectivityHandler connectivityHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hello);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(HelloActivity.this, view);
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onMenuItemSelect(item);
                    }
                });
                p.inflate(R.menu.calculation_selection);
                p.show();
            }
        });
        
        _registerConnectivityReceiver();
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
     *
     * @param item
     * @return
     */
    public boolean onMenuItemSelect(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.item_netto:
                showInputActivity(CALC_TYPE_NETTO);
                return true;
            case R.id.item_brutto:
                showInputActivity(CALC_TYPE_BRUTTO);
                return true;
        }
        return false;
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
            case R.id.action_quit:
                SystemHelper.finish(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

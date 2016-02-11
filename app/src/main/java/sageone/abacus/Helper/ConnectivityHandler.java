package sageone.abacus.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.URI;

import sageone.abacus.R;

/**
 * Created by otomaske on 01.02.2016.
 */
public class ConnectivityHandler extends BroadcastReceiver {

    private ConnectivityManager cm;
    private URI serviceURI;

    private Activity activity;
    private AlertDialog dialog;


    /**
     * The constructor even.
     */
    public ConnectivityHandler(final Activity activity)
    {
        this.activity = activity;

        cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        serviceURI = URI.create(activity.getResources().getString(R.string.api_uri_test));

        dialog = new AlertDialog.Builder(activity)
                .setNegativeButton(
                        activity.getResources().getString(android.R.string.cancel)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SystemHelper.finish(activity);
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (!connectionReady()) {
            showConnectionWarning();
        } else {
            hideDialog();
            return;
        }

        Log.w("ConnectivityHandler", "connectivity not ready");
    }


    /**
     * Checks the internet connection.
     */
    public boolean isOnline()
    {
        try {
            boolean ok = cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnectedOrConnecting();
            if (!ok) {
                Log.w("ConnectivityHandler", "offline or network error:(");
            }
            return ok;
        } catch (Exception e) {
            Log.e("ConnectivityHandler", e.getMessage().toString());
            return false;
        }
    }


    /**
     * Checks if the calculation webservice on online.
     *
     * @return
     */
    public boolean serviceAvailable()
    {
        String host = serviceURI.getHost();
        try {
            boolean res = InetAddress.getByName(host).isReachable(10);
            Log.w("ConnectivityHandler", res + " not reachable");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w("ConnectivityHandler", "Network not reachable [" + host + "]");
        return false;
    }


    /**
     * Checks internet connection and
     * the availability of the api.
     *
     * @return
     */
    public boolean connectionReady()
    {
        try {
            return isOnline();// && serviceAvailable();
        } catch(Exception e) {
            return false;
        }
    }


    /**
     * Renders a modal dialog with
     * connection error message.
     */
    public void showConnectionWarning()
    {
        dialog.setMessage(activity.getResources().getString(R.string.app_connection_not_available));
        dialog.show();
    }


    /**
     * Hides private dialog.
     */
    public void hideDialog()
    {
        if (null != dialog) {
            dialog.hide();
        }
    }
}

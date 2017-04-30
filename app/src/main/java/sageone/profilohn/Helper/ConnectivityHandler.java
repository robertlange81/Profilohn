package sageone.profilohn.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.InetAddress;

import sageone.profilohn.R;

/**
 * Created by profilohn on 01.02.2016.
 */
public class ConnectivityHandler extends BroadcastReceiver {

    private ConnectivityManager cm;
    private String serviceHost;

    private Activity activity;
    private AlertDialog dialog;


    /**
     * The constructor even.
     */
    public ConnectivityHandler(final Activity activity)
    {
        this.activity = activity;
        serviceHost = activity.getResources().getString(R.string.api_uri_host);

        cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        dialog = MessageHelper.dialog(activity, true);
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
        try {
            InetAddress ia = InetAddress.getByName(serviceHost);
            return ia.isReachable(R.integer.api_connection_timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w("ConnectivityHandler", "Network not reachable [" + serviceHost + "]");
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
            return isOnline(); // && serviceAvailable();
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

package com.profilohn.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.profilohn.R;

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

        try {
            if(serviceHost == null || serviceHost == "")
                serviceHost = new RetrieveHost().execute("http://www.robert-lange.eu/host_profilohn.txt").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(serviceHost == null || serviceHost == "")
            serviceHost = activity.getResources().getString(R.string.api_default_uri_host);

        cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        dialog = MessageHelper.dialog(activity, true);
    }

    public String getHost() {
        return serviceHost;
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
            }
            return ok;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean serviceAvailable()
    {
        try {
            InetAddress ia = InetAddress.getByName(serviceHost);
            return ia.isReachable(R.integer.api_connection_timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean connectionReady()
    {
        try {
            return isOnline(); // && serviceAvailable();
        } catch(Exception e) {
            return false;
        }
    }

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


class RetrieveHost extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2500); // timout in 2,5 Sekunden

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String content = "", str;
            while ((str = in.readLine()) != null) {
                content += (str);
            }
            in.close();

            return content;
        } catch (Exception ex) {
            String x = ex.getMessage();
        }

        return null;
    }
}

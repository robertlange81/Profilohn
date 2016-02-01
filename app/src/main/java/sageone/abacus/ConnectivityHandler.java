package sageone.abacus;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by otomaske on 01.02.2016.
 */
public class ConnectivityHandler {

    private ConnectivityManager _cm = null;
    private String _serviceUri = null;

    /**
     * Konschtrucktor.
     *
     */
    public ConnectivityHandler(Context context)
    {
        _cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        _serviceUri = String.valueOf(R.string.api_uri);
    }

    /**
     * Checks the internet connection.
     */
    public boolean isOnline()
    {
        return _cm.getActiveNetworkInfo() != null
                && _cm.getActiveNetworkInfo().isAvailable()
                && _cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Checks if the calculation webservice on online.
     *
     * @return
     */
    public boolean serviceAvailable()
    {
        try {
            return InetAddress.getByName(_serviceUri).isReachable(3);
        } catch (UnknownHostException e){
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}

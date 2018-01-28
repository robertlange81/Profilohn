package com.profilohn.Models;

import retrofit2.Response;
import com.profilohn.Extensions.RetrofitRestClient;
import com.profilohn.Interfaces.ApiInterface;
import com.profilohn.Interfaces.ApiCallbackListener;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import com.profilohn.R;

/**
 * Created by profilohn on 27.01.2016.
 */
public class WebService
{
    private RetrofitRestClient retrofitClient;
    private ApiInterface apiService;
    private ApiCallbackListener webserviceListener;
    private Context context;
    private Call call;

    public static String host;


    /**
     * The constructor.
     */
    public WebService(Context c, ApiCallbackListener listener)
    {
        context = c;
        webserviceListener = listener;

        init();
    }


    /**
     * Initialize the rest client
     * and the instance web service.
     */
    private void init()
    {
        if(host == null || host == "") {
            host = context.getResources().getString(R.string.api_default_uri_host);
        }

        String url  = context.getResources().getString(R.string.api_uri_base);
        String credentials = context.getResources().getString(R.string.basic_credentials);
        int timeout = context.getResources().getInteger(R.integer.api_connection_timeout);

        String apiUriBase = host + url;

        retrofitClient = new RetrofitRestClient();
        apiService = retrofitClient.RetrofitRestClient(apiUriBase, credentials, timeout)
                    .create(ApiInterface.class);
    }


    /**
     * Calculates given data via web service call.
     *
     * @param data
     */
    public void Calculate(CalculationInput data)
    {
        // Log.v("ServiceCall", "Initialize calculation ..");
        call = apiService.Calc(data);

        call.enqueue(new Callback<Calculation>() {
            @Override
            public void onResponse(Call<Calculation> call, Response<Calculation> response) {
                int code = response.code();

                if (response.isSuccess()) {
                    webserviceListener.responseFinishCalculation(response.body());
                } else {
                    webserviceListener.responseFailedCalculation(context.getResources().getString(R.string.exception_status_code));
                }
            }

            @Override
            public void onFailure(Call<Calculation> call, Throwable throwable) {
                webserviceListener.responseFailedCalculation(context.getResources().getString(R.string.app_api_error));
            }
        });

    }


    /**
     * Fetch all available Health Insurances
     * by calling web service via rest client.
     */
    public void Insurances()
    {
        Call<Insurances> call = apiService.Insurances();

        call.enqueue(new Callback<Insurances>() {
            @Override
            public void onResponse(Call<Insurances> call, Response<Insurances> response) {

                int code = response.code();
                String message = null;

                switch (code) {
                    case 200:
                        webserviceListener.responseFinishInsurances(response.body());
                        break;
                    case 401:
                        message = context.getResources().getString(R.string.exception_http_auth);
                        webserviceListener.responseFailedInsurances(message);
                        break;
                    default:
                        message = context.getResources().getString(R.string.exception_status_code);
                        webserviceListener.responseFailedInsurances(message);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Insurances> call, Throwable t) {
                String err = t.getMessage().toString();
                webserviceListener.responseFailedInsurances(err);
            }
        });
    }


    /**
     * Cancel the active call.
     */
    public void cancel()
    {
        call.cancel();
    }

}

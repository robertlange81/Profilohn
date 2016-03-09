package sageone.abacus.Models;

import retrofit2.Response;
import sageone.abacus.Activities.HelloActivity;
import sageone.abacus.Activities.InputActivity;
import sageone.abacus.Exceptions.StatusCodeException;
import sageone.abacus.Exceptions.WebServiceFailureException;
import sageone.abacus.Extensions.RetrofitRestClient;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Helper.SystemHelper;
import sageone.abacus.Interfaces.AbacusApiInterface;
import sageone.abacus.Interfaces.ApiCallbackListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import sageone.abacus.R;

/**
 * Created by otomaske on 27.01.2016.
 */
public class WebService
{
    private RetrofitRestClient retrofitClient;
    private AbacusApiInterface apiService;
    private ApiCallbackListener webserviceListener;
    private static WebService Instance;
    private Context context;
    private Call call;

    public static synchronized WebService getInstance(Context c, ApiCallbackListener listener)
    {
        if (null == Instance) {
            Instance = new WebService(c, listener);
        }
        return Instance;
    }


    /**
     * The constructor.
     */
    private WebService(Context c, ApiCallbackListener listener)
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
        String proto = context.getResources().getString(R.string.api_uri_proto);
        String host = context.getResources().getString(R.string.api_uri_host);
        String url  = context.getResources().getString(R.string.api_uri_base);
        String credentials = context.getResources().getString(R.string.basic_credentials);

        String apiUriBase = proto + host + url;

        retrofitClient = new RetrofitRestClient();
        apiService = retrofitClient.RetrofitRestClient(apiUriBase, credentials, context.getResources().getInteger(R.integer.api_connection_timeout))
                .create(AbacusApiInterface.class);
    }


    /**
     * Calculates given data via web service call.
     *
     * @param data
     */
    public void Calculate(CalculationInput data)
    {
        Log.v("ServiceCall", "Initialize calculation ..");
        call = apiService.Calc(data);

        call.enqueue(new Callback<Calculation>() {
            @Override
            public void onResponse(Call<Calculation> call, Response<Calculation> response) {
                if (response.isSuccess()) {
                    Log.v("WebService", "Calculation successfully finished. Start result view ..");
                    webserviceListener.responseFinishCalculation(response.body());
                } else {
                    Log.e("WebService", "Calculation failed with NOT SUCCESS.");
                    webserviceListener.responseFailedCalculation(context.getResources().getString(R.string.exception_status_code));
                }
            }

            @Override
            public void onFailure(Call<Calculation> call, Throwable throwable) {
                Log.e("WebService", "Failure on calculation. " + throwable.getStackTrace().toString());
                webserviceListener.responseFailedCalculation(context.getResources().getString(R.string.app_api_error));
            }
        });

    }


    /**
     * Fetch all available Health Insurances
     * by calling web service via rest client.
     *
     * @throws StatusCodeException
     */
    public void Insurances() throws StatusCodeException
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
                        Log.e("WebService", "Status code " + code);
                        message = context.getResources().getString(R.string.exception_http_auth);
                        new StatusCodeException(message);
                        break;
                    default:
                        Log.e("WebService", "Status code " + code);
                        message = context.getResources().getString(R.string.exception_status_code);
                        new StatusCodeException(message);
                        break;
                }

                Log.i("WebService", "Fetch insurances successfully finished");
            }

            @Override
            public void onFailure(Call<Insurances> call, Throwable t) {
                String err = t.getMessage().toString();
                Log.e("WebService", err);
                String message = context.getResources().getString(R.string.exception_status_code);
                new StatusCodeException(message);
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

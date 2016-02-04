package sageone.abacus;

import retrofit.Response;
import sageone.abacus.Exceptions.StatusCodeException;
import sageone.abacus.Exceptions.WebServiceFailureException;
import sageone.abacus.Interfaces.AbacusApiInterface;
import sageone.abacus.Interfaces.WebServiceListener;
import sageone.abacus.Models.CalculationData;
import sageone.abacus.Models.Insurances;

import android.content.Context;
import android.util.Log;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by otomaske on 27.01.2016.
 */
public class WebService
{
    private RetrofitRestClient retrofitClient;
    private AbacusApiInterface apiService;
    private static WebService Instance;
    private Context context;

    public Insurances insurancesResponse;
    private WebServiceListener webserviceListener;

    public static synchronized WebService getInstance(Context c, WebServiceListener listener)
    {
        if (null == Instance) {
            Instance = new WebService(c, listener);
        }
        return Instance;
    }

    /**
     * The constructor.
     */
    private WebService(Context c, WebServiceListener listener)
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
        String apiUriBase = context.getResources().getString(R.string.api_uri_base);

        retrofitClient = new RetrofitRestClient();
        apiService = retrofitClient.RetrofitRestClient(apiUriBase).create(AbacusApiInterface.class);

    }

    /**
     * Calculates given data via web service call.
     *
     * @param data
     */
    public void Calculate(CalculationData data)
    {

    }

    /**
     * Fetch all available Health Insurances
     * by calling web service via rest client.
     *
     * @throws StatusCodeException
     */
    public void Insurances() throws StatusCodeException, WebServiceFailureException
    {
        Call<Insurances> call = apiService.Insurances();

        call.enqueue(new Callback<Insurances>() {
            @Override
            public void onResponse(Response<Insurances> response, Retrofit retrofit) {
                int statusCode = response.code();
                if (!response.isSuccess()) {
                    new StatusCodeException();
                }
                Log.d("API", "finish");
                webserviceListener.responseFinishInsurances(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                new WebServiceFailureException();
            }
        });
    }

}

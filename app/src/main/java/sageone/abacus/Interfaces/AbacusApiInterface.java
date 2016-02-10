package sageone.abacus.Interfaces;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.CalculationInputData;
import sageone.abacus.Models.Insurances;

/**
 * Created by otomaske on 04.02.2016.
 */
public interface AbacusApiInterface
{
    @GET("insurances")
    Call<Insurances> Insurances();

    @GET("success")
    Call<Calculation> Success();

    @POST("calc")
    Call<Calculation> Calc(@Body CalculationInputData data);
}

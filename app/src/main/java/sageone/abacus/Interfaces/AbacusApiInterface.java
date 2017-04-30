package sageone.abacus.Interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.CalculationInput;
import sageone.abacus.Models.CalculationInputData;
import sageone.abacus.Models.Insurances;

/**
 * Created by profilohn on 04.02.2016.
 */
public interface AbacusApiInterface
{
    @GET("insurances")
    Call<Insurances> Insurances();

    @GET("success")
    Call<Calculation> Success();

    @POST("calc")
    Call<Calculation> Calc(@Body CalculationInput data);
}

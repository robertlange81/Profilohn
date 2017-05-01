package com.profilohn.Interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import com.profilohn.Models.Calculation;
import com.profilohn.Models.CalculationInput;
import com.profilohn.Models.Insurances;

/**
 * Created by profilohn on 04.02.2016.
 */
public interface ApiInterface
{
    @GET("insurances")
    Call<Insurances> Insurances();

    @GET("success")
    Call<Calculation> Success();

    @POST("calc")
    Call<Calculation> Calc(@Body CalculationInput data);
}

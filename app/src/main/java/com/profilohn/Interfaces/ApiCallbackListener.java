package com.profilohn.Interfaces;

import com.profilohn.Models.Calculation;
import com.profilohn.Models.Insurances;

/**
 * Created by profilohn on 04.02.2016.
 */
public interface ApiCallbackListener
{
    void responseFinishInsurances(Insurances insurances);
    void responseFailedInsurances(String message);
    void responseFinishCalculation(Calculation calculation);
    void responseFailedCalculation(String message);
}

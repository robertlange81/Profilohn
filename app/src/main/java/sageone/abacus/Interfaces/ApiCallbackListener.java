package sageone.abacus.Interfaces;

import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.Insurances;

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

package sageone.abacus.Interfaces;

import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.Insurances;

/**
 * Created by otomaske on 04.02.2016.
 */
public interface ApiCallbackListener
{
    void responseFinishInsurances(Insurances insurances);
    void responseFinishCalculation(Calculation calculation);
}

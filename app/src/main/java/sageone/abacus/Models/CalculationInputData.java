package sageone.abacus.Models;

import sageone.abacus.Helper.CalculationInputHelper;

/**
 * Created by otomaske on 09.02.2016.
 *
 * The calculation request data.
 */
public class CalculationInputData {

    public String Berechnungsart = CalculationInputHelper.WAGE_TYPE_NET;
    public String Zeitraum = CalculationInputHelper.WAGE_PERIOD_MONTH;
    public int StKl = 1;
    public int Bundesland = 1;
    public Long KKBetriebsnummer;
    public Double Brutto = 0.0;
    public boolean Kirche = false;
    public Double KindFrei = 0.0;
    public boolean KindU23 = false;
}

package sageone.abacus.Models;

import java.util.Date;

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
    public Integer KKBetriebsnummer;
    public Double Brutto = 0.0;
    public boolean Kirche = false;
    public Double KindFrei = 0.0;
    public boolean KindU23 = false;
    public Double StFreibetrag = 0.0;
    public int AbrJahr = 1970;
    public int KV = 1;
    public int RV = 1;
    public int AV = 1;
    public boolean Gleit = false;
    public float AzWo = 40;
}

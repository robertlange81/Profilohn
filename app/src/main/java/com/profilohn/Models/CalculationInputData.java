package com.profilohn.Models;

import java.util.Calendar;

import com.profilohn.Helper.CalculationInputHelper;

/**
 * Created by profilohn on 09.02.2016.
 *
 * The calculation request data.
 */
public class CalculationInputData {


    public String Berechnungsart = CalculationInputHelper.WAGE_TYPE_NET;
    public String Zeitraum = CalculationInputHelper.WAGE_PERIOD_MONTH;
    public int StKl = 1;
    public int Bundesland = 1;
    public Integer KKBetriebsnummer;
    public String KK_text = "";
    public Double Brutto = 0.0;
    public boolean Kirche = false;
    public Double KindFrei = 0.0;
    public boolean KindU23 = false;
    public Double StFreibetrag = 0.0;
    public int AbrJahr = Calendar.YEAR;
    public int KV = 1;
    public int RV = 1;
    public int AV = 1;
    public int PV = 1;
    public boolean Gleit = true;
    public float AzWo = 40;
    public int Beschaeftigungsart = 0;
    public boolean dummyInsurance = false;
    public boolean abwaelzung_pauschale_steuer = false;
    public boolean hatAltersvorsorge = false;
    public Double Altersvorsorge_summe = 0.0;
    public Double Altersvorsorge_zuschuss = 0.0;
    public Double Altersvorsorge_pflichtig = 0.0;
    public boolean hatFirmenwagen = false;
    public Integer Firmenwagen_summe = 0;
    public Integer Firmenwagen_km = 0;
    public Double Firmenwagen_pflichtig = 0.0;
    public boolean hatPfaendung = false;
    public Integer unterhaltspflPers;
    public Double pfaendungsfreierBetrag = 0.0;
}

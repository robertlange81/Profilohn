package sageone.abacus.Models;

import android.app.Activity;

import sageone.abacus.Exceptions.FormatException;
import sageone.abacus.Exceptions.ValidationException;
import sageone.abacus.R;

/**
 * Created by otomaske on 04.02.2016.
 */
public class CalculationData {

    public String LohnsteuerPflBrutto;
    public String SVPflBrutto;
    public String Lohnsteuer;
    public String Soli;
    public String Kirchensteuer;
    public String KrankenversicherungAN;
    public String RentenversicherungAN;
    public String ArbeitslosenversicherungAN;
    public String PflegeversicherungAN;
    public String KrankenversicherungAG;
    public String RentenversicherungAG;
    public String ArbeitslosenversicherungAG;
    public String PflegeversicherungAG;
    public String Umlage1;
    public String Umlage2;
    public String Netto;
    public String Auszahlung;
    public String AGAnteil;
    public String IGU;
    public String Sozialabgaben;
    public int BruttoDecimal;
    public sageone.abacus.Models.Trace Trace;

    private Activity a;

    public CalculationData(Activity activity)
    {
        a = activity;
    }

    public void validate() throws ValidationException
    {
        if (_nullOrEmpty(Netto)) {
            throw new ValidationException(a.getResources().getString(R.string.validation_error_wage));
        }

    }

    private boolean _nullOrEmpty(String data)
    {
        return (null == data || 0 == data.length());
    }

    public void format() throws FormatException
    {
        try {
            Netto = String.valueOf(Double.valueOf(Netto.replaceAll("\\D", "")) / 100);

        } catch (Exception e) {
            throw new FormatException(String.valueOf(R.string.format_error));
        }
    }

}
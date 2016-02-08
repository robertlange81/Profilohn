package sageone.abacus.Models;

import sageone.abacus.Exceptions.ValidationException;

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

    public boolean validate() throws ValidationException
    {
        if (null == Netto) {
            throw new ValidationException("No valid wage value");
        }

        return true;
    }

}
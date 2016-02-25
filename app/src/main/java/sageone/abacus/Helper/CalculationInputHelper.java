package sageone.abacus.Helper;

import android.app.Activity;
import java.util.HashMap;

import sageone.abacus.Exceptions.ValidationException;
import sageone.abacus.Models.CalculationInputData;
import sageone.abacus.R;

/**
 * Created by otomaske on 10.02.2016.
 */
public class CalculationInputHelper
{
    private Activity a;
    public CalculationInputData data;

    public static final String WAGE_TYPE_GROSS   = "Bruttolohn";
    public static final String WAGE_TYPE_NET     = "Nettolohn";
    public static final String WAGE_PERIOD_YEAR  = "y";
    public static final String WAGE_PERIOD_MONTH = "m";


    public CalculationInputHelper(Activity a, CalculationInputData data)
    {
        this.a = a;
        this.data = data;
    }


    /**
     * Validates the inputs.
     *
     * @throws ValidationException
     */
    public void validate() throws ValidationException
    {
        if (_nullOrEmpty(data.Brutto)) {
            String message = a.getResources().getString(R.string.validation_error_wage);
            throw new ValidationException(message);
        }
        if (data.KKBetriebsnummer == -1) {
            String message = a.getResources().getString(R.string.validation_error_insurance);
            throw new ValidationException(message);
        }
    }


    /**
     * Checks data if bigger then null and 0.
     *
     * @param data
     * @return
     */
    private boolean _nullOrEmpty(Double data)
    {
        return (null == data || 0.0 == data);
    }


    /**
     * Prepares the child u23 boolean value.
     *
     * @param value
     */
    public void setKindFrei(Double value)
    {
        data.KindFrei = value;
        data.KindU23 = 0 < value ? true : false;
    }


    /**
     * Sets the tax class.
     *
     * @param taxClass
     */
    public void setStKl(String taxClass)
    {
        data.StKl = this.translateTaxClass(taxClass);
    }


    /**
     * Internal tax class translator.
     *
     * @param taxClass
     * @return
     */
    private int translateTaxClass(String taxClass)
    {
        HashMap<String, Integer> taxClasses = new HashMap<String, Integer>();
        taxClasses.put("I",   1);
        taxClasses.put("II",  2);
        taxClasses.put("III", 3);
        taxClasses.put("IV",  4);
        taxClasses.put("V",   5);
        taxClasses.put("VI",  6);

        return taxClasses.get(taxClass);
    }

    /**
     * Help function for translate
     * the states.
     *
     * @param state
     */
    public void setBundesland(String state)
    {
        data.Bundesland = this.translateState(state);
    }


    /**
     * States translator.
     *
     * @param state
     * @return
     */
    private int translateState(String state)
    {
        HashMap<String, Integer> states = new HashMap<String, Integer>();
        states.put("Baden-Württemberg", 1);
        states.put("Bayern", 2);
        states.put("Berlin-West", 3);
        states.put("Brandenburg", 4);
        states.put("Bremen", 5);
        states.put("Hamburg", 6);
        states.put("Hessen", 7);
        states.put("Mecklenburg-Vorpommern", 8);
        states.put("Niedersachsen", 9);
        states.put("Nordrhein-Westfalen", 10);
        states.put("Rheinland-Pfalz", 11);
        states.put("Saarland", 12);
        states.put("Sachsen", 13);
        states.put("Sachsen-Anhalt", 14);
        states.put("Schleswig-Holstein", 15);
        states.put("Thüringen", 16);
        states.put("Berlin-West", 30);

        return states.get(state);
    }

}

package com.profilohn.Helper;

import android.app.Activity;

import java.util.HashMap;

import com.profilohn.Exceptions.ValidationException;
import com.profilohn.Exceptions.ValidationInsuranceException;
import com.profilohn.Models.CalculationInputData;
import com.profilohn.R;

/**
 * Created by profilohn on 10.02.2016.
 */
public class CalculationInputHelper
{
    private Activity a;
    public CalculationInputData data;

    public static final String WAGE_TYPE_GROSS   = "Bruttolohn";
    public static final String WAGE_TYPE_NET     = "Nettolohn";
    public static final String WAGE_PERIOD_YEAR  = "y";
    public static final String WAGE_PERIOD_MONTH = "m";

    public static final HashMap<String, Integer> statesToInt = new HashMap<String, Integer>();
    public static final HashMap<Integer, String> intToState = new HashMap<Integer, String>();

    public CalculationInputHelper(Activity a, CalculationInputData data)
    {
        this.a = a;
        this.data = data;

        intToState.put(1, a.getResources().getString(R.string.bg));
        intToState.put(2, a.getResources().getString(R.string.by));
        intToState.put(3, a.getResources().getString(R.string.bw));
        intToState.put(4, a.getResources().getString(R.string.bb));
        intToState.put(5, a.getResources().getString(R.string.bn));
        intToState.put(6, a.getResources().getString(R.string.hh));
        intToState.put(7, a.getResources().getString(R.string.he));
        intToState.put(8, a.getResources().getString(R.string.mv));
        intToState.put(9, a.getResources().getString(R.string.ns));
        intToState.put(10, a.getResources().getString(R.string.nw));
        intToState.put(11, a.getResources().getString(R.string.rp));
        intToState.put(12, a.getResources().getString(R.string.sl));
        intToState.put(13, a.getResources().getString(R.string.sn));
        intToState.put(14, a.getResources().getString(R.string.sa));
        intToState.put(15, a.getResources().getString(R.string.sh));
        intToState.put(16, a.getResources().getString(R.string.th));
        intToState.put(30, a.getResources().getString(R.string.bo));

        statesToInt.put(intToState.get(1), 1);
        statesToInt.put(intToState.get(2), 2);
        statesToInt.put(intToState.get(3), 3);
        statesToInt.put(intToState.get(4), 4);
        statesToInt.put(intToState.get(5), 5);
        statesToInt.put(intToState.get(6), 6);
        statesToInt.put(intToState.get(7), 7);
        statesToInt.put(intToState.get(8), 8);
        statesToInt.put(intToState.get(9), 9);
        statesToInt.put(intToState.get(10), 10);
        statesToInt.put(intToState.get(11), 11);
        statesToInt.put(intToState.get(12), 12);
        statesToInt.put(intToState.get(13), 13);
        statesToInt.put(intToState.get(14), 14);
        statesToInt.put(intToState.get(15), 15);
        statesToInt.put(intToState.get(16), 16);
        statesToInt.put(intToState.get(30), 30);

    }


    /**
     * Validates the inputs.
     *
     * @throws ValidationException
     * @return boolean
     */
    public boolean validate() throws ValidationException
    {
        if (_nullOrEmpty(data.Brutto)) {
            String message = a.getResources().getString(R.string.validation_error_wage);
            throw new ValidationException(message);
        }

        data.dummyInsurance = false;
        int aok_bayern = 87540905;
        int knappschaft = 98000006;
        if(data.KKBetriebsnummer == -1) {
            if (data.KV != 6 && (data.KV != 0 || data.PV != 0)) {
                String message = a.getResources().getString(R.string.validation_error_insurance);
                throw new ValidationInsuranceException(message);
            } else {
                data.KKBetriebsnummer = 67450665;
                data.dummyInsurance = true;
            }
        }


        return true;
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
        return statesToInt.get(state);
    }

    public String retranslateState(int state)
    {
        return intToState.get(state);
    }
}

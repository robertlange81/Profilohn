package com.profilohn.Helper;

import android.app.Activity;
import android.content.res.Resources;

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


    public CalculationInputHelper(Activity a, CalculationInputData data)
    {
        this.a = a;
        this.data = data;
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
        HashMap<String, Integer> states = new HashMap<String, Integer>();
        states.put(a.getResources().getString(R.string.bg), 1);
        states.put(a.getResources().getString(R.string.by), 2);
        states.put(a.getResources().getString(R.string.bw), 3);
        states.put(a.getResources().getString(R.string.bb), 4);
        states.put(a.getResources().getString(R.string.bn), 5);
        states.put(a.getResources().getString(R.string.hh), 6);
        states.put(a.getResources().getString(R.string.he), 7);
        states.put(a.getResources().getString(R.string.mv), 8);
        states.put(a.getResources().getString(R.string.ns), 9);
        states.put(a.getResources().getString(R.string.nw), 10);
        states.put(a.getResources().getString(R.string.rp), 11);
        states.put(a.getResources().getString(R.string.sl), 12);
        states.put(a.getResources().getString(R.string.sn), 13);
        states.put(a.getResources().getString(R.string.sa), 14);
        states.put(a.getResources().getString(R.string.sh), 15);
        states.put(a.getResources().getString(R.string.th), 16);
        states.put(a.getResources().getString(R.string.bo), 30);

        return states.get(state);
    }

    public static String retranslateState(int state)
    {
        HashMap<Integer, String> states = new HashMap<Integer, String>();
        states.put(1, Resources.getSystem().getString(R.string.bg));
        states.put(2, Resources.getSystem().getString(R.string.by));
        states.put(3, Resources.getSystem().getString(R.string.bw));
        states.put(4, Resources.getSystem().getString(R.string.bb));
        states.put(5, Resources.getSystem().getString(R.string.bn));
        states.put(6, Resources.getSystem().getString(R.string.hh));
        states.put(7, Resources.getSystem().getString(R.string.he));
        states.put(8, Resources.getSystem().getString(R.string.mv));
        states.put(9, Resources.getSystem().getString(R.string.ns));
        states.put(10, Resources.getSystem().getString(R.string.nw));
        states.put(11, Resources.getSystem().getString(R.string.rp));
        states.put(12, Resources.getSystem().getString(R.string.sl));
        states.put(13, Resources.getSystem().getString(R.string.sn));
        states.put(14, Resources.getSystem().getString(R.string.sa));
        states.put(15, Resources.getSystem().getString(R.string.sh));
        states.put(16, Resources.getSystem().getString(R.string.th));
        states.put(30, Resources.getSystem().getString(R.string.bo));

        return states.get(state);
    }
}

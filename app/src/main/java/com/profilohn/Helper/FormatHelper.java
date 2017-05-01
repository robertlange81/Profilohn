package com.profilohn.Helper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.profilohn.Exceptions.FormatException;

/**
 * Created by profilohn on 17.02.2016.
 */
public class FormatHelper
{
    public static NumberFormat deDE = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    public final static String currency(String text) throws FormatException
    {
        try {
            deDE.setMinimumFractionDigits(2);
            deDE.setMaximumFractionDigits(2);
            Double d = Double.valueOf(text.replace(".", "").replace(",", "."));
            return deDE.format(d);
        } catch (Exception e) {
            throw new FormatException();
        }
    }


    public final static Double toDouble(String s)
    {
        return Double.valueOf(s.replace(".", "").replace(",", "."));
    }


    public final static String percent(String one, String two)
    {
        Double diff = FormatHelper.toDouble(one)
                / FormatHelper.toDouble(two) *100 - 100;
        DecimalFormat form = new DecimalFormat("#.##");

        String prefix = "";
        if(0 < diff) {
            prefix = "+";
        }

        return prefix + form.format(diff) + " %";
    }

}
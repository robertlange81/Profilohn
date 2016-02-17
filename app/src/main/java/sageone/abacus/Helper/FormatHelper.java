package sageone.abacus.Helper;
import java.text.NumberFormat;
import java.util.Locale;

import sageone.abacus.Exceptions.FormatException;

/**
 * Created by otomaske on 17.02.2016.
 */
public class FormatHelper
{
    public static NumberFormat deDE = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    public final static String currency(String text) throws FormatException
    {
        try {
            Double d = Double.valueOf(text.replace(".", "").replace(",", "."));
            return deDE.format(d);
        } catch (Exception e) {
            throw new FormatException();
        }
    }

}
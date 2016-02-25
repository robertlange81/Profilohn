package sageone.abacus.Helper;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by otomaske on 25.02.2016.
 */
public class DecimalDigitsInputHelper implements InputFilter {

    private final int decimalDigits;

    /**
     * Constructor.
     *
     * @param decimalDigits maximum decimal digits
     */
    public DecimalDigitsInputHelper(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {


        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source.equals(".") || source.equals(","))
            {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }
}

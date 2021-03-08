package ru.true_ip.trueip.utils.input_filters;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by ektitarev on 10.09.2018.
 */

public class ApartmentNumberInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        String inputString = charSequence.toString();
        if (inputString.matches("[^a-zA-Z0-9\\u0400-\\u04FF]")) {
            return "";
        }
        return null;
    }
}

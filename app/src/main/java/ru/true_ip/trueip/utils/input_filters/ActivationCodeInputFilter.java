package ru.true_ip.trueip.utils.input_filters;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by ektitarev on 06.09.2018.
 */

public class ActivationCodeInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        String inputText = charSequence.toString();
        if(!inputText.matches("[a-zA-Z0-9-]+"))
            return "";
        return null;
    }
}

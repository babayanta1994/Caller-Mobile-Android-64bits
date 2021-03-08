package ru.true_ip.trueip.utils.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by ektitarev on 06.09.2018.
 */

public class ActivationCodeTextWatcher implements TextWatcher {

    private static final String TAG = ActivationCodeTextWatcher.class.getSimpleName();

    private EditText targetEditText;
    private int length = 0;

    public ActivationCodeTextWatcher(EditText targetEditText) {
        this.targetEditText = targetEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String initText = editable.toString();
        String code = initText.replaceAll("-", "");

        int selectionStartPos = targetEditText.getSelectionStart();
        int selectionEndPos = targetEditText.getSelectionEnd();

        int selectionStartFormatOffset = selectionStartPos - selectionStartPos / 4 - 1;
        int selectionEndFormatOffset = selectionEndPos - selectionEndPos / 4 - 1;

        targetEditText.removeTextChangedListener(this);

        if (code.length() > 4) {
            String formattedCode = code.replaceAll("(\\w{4})(?=\\w)", "$1-");
            targetEditText.getText().clear();
            targetEditText.append(formattedCode.toUpperCase(Locale.getDefault()));

            try {
                if (formattedCode.length() > length && (selectionStartPos - selectionStartPos / 4) % 4 == 0) {
                    targetEditText.setSelection(selectionStartFormatOffset + selectionStartPos / 4 + 2, selectionEndFormatOffset + selectionEndPos / 4 + 2);
                } else {
                    targetEditText.setSelection(selectionStartFormatOffset + selectionStartPos / 4 + 1, selectionEndFormatOffset + selectionEndPos / 4 + 1);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        } else {
            targetEditText.getText().clear();
            targetEditText.append(code.toUpperCase(Locale.getDefault()));

            try {
                targetEditText.setSelection(selectionStartPos, selectionEndPos);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        length = targetEditText.length();

        targetEditText.addTextChangedListener(this);
    }
}

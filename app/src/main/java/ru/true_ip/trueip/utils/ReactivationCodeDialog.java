package ru.true_ip.trueip.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.utils.input_filters.ActivationCodeInputFilter;
import ru.true_ip.trueip.utils.text_watchers.ActivationCodeTextWatcher;

/**
 * Created by ektitarev on 05.09.2018.
 */

public class ReactivationCodeDialog extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {

    public static ReactivationCodeDialog getInstance(String title, OnClickListener listener) {
        ReactivationCodeDialog reactivationCodeDialog = new ReactivationCodeDialog();
        reactivationCodeDialog.setTitle(title);
        if (listener != null) {
            reactivationCodeDialog.addOnClickListener(listener);
        }
        return reactivationCodeDialog;
    }

    public static ReactivationCodeDialog getInstance() {
        return getInstance("", null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (objectAddress != null) {
            objectAddress.setText(title);
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            dialog.dismiss();
            if (onClickListeners != null) {
                for (OnClickListener l : onClickListeners) {
                    l.onCancelClick(getDialog());
                }
            }
            return true;
        }
        return false;
    }

    public interface OnClickListener {
        void onSubmitClick (DialogInterface dialogInterface, CharSequence reactivationCode);
        void onCancelClick (DialogInterface dialogInterface);
    }

    private String title;
    private EditText reactivationCode;
    private TextView objectAddress;
    private Set<OnClickListener> onClickListeners = new HashSet<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(this);

        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reactivation_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        objectAddress = view.findViewById(R.id.object_address);
        objectAddress.setText(title);

        reactivationCode = view.findViewById(R.id.reactivation_code);
        reactivationCode.setFilters(new InputFilter[] { new ActivationCodeInputFilter(), new InputFilter.LengthFilter(Constants.ACTIVATION_CODE_LENGTH + 3) });
        reactivationCode.addTextChangedListener(new ActivationCodeTextWatcher(reactivationCode));

        Button submit = view.findViewById(R.id.submit);
        Button cancel = view.findViewById(R.id.cancel);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void addOnClickListener(OnClickListener onClickListener) {
        this.onClickListeners.add(onClickListener);
    }

    public void removeOnClickListener(OnClickListener onClickListener) {
        this.onClickListeners.remove(onClickListener);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.submit:
                if (onClickListeners != null) {
                    for (OnClickListener l : onClickListeners) {
                        l.onSubmitClick(getDialog(), reactivationCode.getText());
                    }
                }
                break;
            case R.id.cancel:
                if (onClickListeners != null) {
                    for (OnClickListener l : onClickListeners) {
                        l.onCancelClick(getDialog());
                    }
                }
                break;
        }
    }
}

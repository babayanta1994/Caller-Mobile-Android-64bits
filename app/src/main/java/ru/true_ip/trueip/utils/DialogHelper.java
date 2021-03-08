package ru.true_ip.trueip.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;

/**
 * Created by Eugen on 20.10.2017.
 */

public class DialogHelper {

    public static abstract class DialogOption implements DialogInterface.OnClickListener {
        private CharSequence title;
        public DialogOption (CharSequence title) {
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    public static void createDeleteDialog(Context context, @StringRes int stringId, DialogInterface.OnClickListener onClickListener) {
        Activity activity = getActivity(context);
        if (activity != null && !activity.isDestroyed()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setMessage(stringId)
                    .setPositiveButton(R.string.text_yes, onClickListener)
                    .setNegativeButton(R.string.text_no, ((dialog, which) -> dialog.dismiss()))
                    .create();
            alertDialog.show();
        }
    }

    public static void createExplanationDialog(Context context, @StringRes int stringId) {
        Activity activity = getActivity(context);
        if (activity != null && !activity.isDestroyed()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setMessage(stringId)
                    .setNeutralButton(R.string.text_ok, (dialog, i) -> dialog.dismiss())
                    .create();
            alertDialog.show();
        }
    }

    public static void createErrorDialog(Context context, String title, String message) {
        createErrorDialog(context, title, message, (dialog, which) -> dialog.dismiss());
    }

    public static void createErrorDialog(Context context, String title, String message, DialogInterface.OnClickListener action) {
        Activity activity = getActivity(context);
        if (activity != null && !activity.isDestroyed()) {
            AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.text_ok, action)
                    .create();
            alertDialog.show();
        }
    }

    public static void createOptionsDialog(Context context,
                                           CharSequence title,
                                           CharSequence message,
                                           DialogOption positive,
                                           DialogOption negative) {
        Activity activity = getActivity(context);
        if (activity != null && !activity.isDestroyed()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positive.getTitle(), positive)
                    .setNegativeButton(negative.getTitle(), negative)
                    .create();
            alertDialog.show();
        }
    }

    public static void createOptionsDialog(Context context,
                                           @StringRes int title,
                                           @StringRes int message,
                                           DialogOption positive,
                                           DialogOption negative) {
        DialogHelper.createOptionsDialog(context,
                context.getString(title),
                context.getString(message),
                positive,
                negative);
    }

    public static void createOptionsDialog(Context context,
                                           CharSequence message,
                                           DialogOption positive,
                                           DialogOption negative) {
        Activity activity = getActivity(context);
        if (activity != null && !activity.isDestroyed()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(positive.getTitle(), positive)
                    .setNegativeButton(negative.getTitle(), negative)
                    .create();
            alertDialog.show();
        }
    }

    public static void createOptionsDialog(Context context,
                                           @StringRes int message,
                                           DialogOption positive,
                                           DialogOption negative) {
        DialogHelper.createOptionsDialog(context,
                context.getString(message),
                positive,
                negative);
    }

    public static void createErrorDialogForActivity(BaseActivity context, String title, String message) {
        if (!context.isDestroyed()) {
            AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.text_ok, ((dialog, which) -> dialog.dismiss()))
                    .create();
            alertDialog.show();
        }
    }


    public static void createInfoDialog(Context context, String title, String message) {
        createErrorDialog(context, title, message);
    }

    public static void createInfoDialog(Context context, @StringRes int title, @StringRes int message) {
        createErrorDialog(context,
                context.getResources().getString(title),
                context.getResources().getString(message));
    }

    public static ProgressDialog createProgressDialog(Context context, CharSequence message) {
        ProgressDialog dialog = new ProgressDialog(context);

        if (message != null && message.length() > 0) {
            dialog.setMessage(message);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setIndeterminate(true);

        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            }
            return false;
        });

        return dialog;
    }

    public static Activity getActivity(Context context) {
        Activity activity = null;
        while(context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity) context;
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        return activity;
    }
}

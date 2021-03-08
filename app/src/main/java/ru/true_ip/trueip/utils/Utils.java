package ru.true_ip.trueip.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import ru.true_ip.trueip.models.responses.ErrorApiResponse;

/**
 * Created by Eugen on 23.10.2017.
 *
 */

public class Utils {

    private static final String LOGCAT = Utils.class.getSimpleName();

    public static String convertImageToBase64String(File imageFile) {
        if (imageFile == null) return "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap convertBase64StringToImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String createRTSPLink(String username, String password, String ipAddress, String port) {
        return String.format(Constants.RSTP_BASE_FORMAT, username, password, ipAddress, port);
    }

    public static String getPathByMediaUri(Context context, Uri uri) {
        String documentId = DocumentsContract.getDocumentId(uri);
        String[] documentIdParts = documentId.split(":");

        if (documentIdParts.length > 1) {
            String type = documentIdParts[0];

            if ("image".equals(type)) {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            String selection = "_id=?";
            String[] selectionArgs = new String[] { documentIdParts[1] };
            String[] projection = { MediaStore.Images.Media.DATA };

            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uri.toString();
    }


    @NonNull
    public static String getDetailedErrorMessage(ErrorApiResponse errorApiResponse) {
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, String[]> detailedMessagesMap = errorApiResponse.getDetailedErrors();
        if (detailedMessagesMap != null && !detailedMessagesMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : detailedMessagesMap.entrySet()) {
                String[] detailedErrors = entry.getValue();
                if (detailedErrors != null && detailedErrors.length > 0) {
                    for (String message : detailedErrors) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append(message);
                    }
                }
            }
        } else {
            stringBuilder.append(errorApiResponse.getError());
        }

        return stringBuilder.toString();
    }


    public static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static void closeSoftKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View focusedView = activity.getCurrentFocus();
            if (inputMethodManager != null && focusedView != null) {
                inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
    }

    @NonNull
    public static String dateToTimestamp(String dateString, String stringFormat) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(stringFormat, Locale.getDefault());
        Date date = format.parse(dateString);
        return String.valueOf(date.getTime());
    }

    public static String timestampToDate(String timestampString, String stringFormat) {
        SimpleDateFormat format = new SimpleDateFormat(stringFormat, Locale.getDefault());
        Date date = new Date();
        date.setTime(Long.parseLong(timestampString));

        return format.format(date);
    }

    public static boolean checkNetworkAvaibility(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }

        return false;
    }
}

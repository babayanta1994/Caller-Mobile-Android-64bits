package ru.true_ip.trueip.service.service;

import android.util.Log;

import ru.true_ip.trueip.BuildConfig;

/**
 *
 * Created by Andrey Filimonov on 23.11.2017.
 */

public class DefaultLoggerDelegate implements Logger.LoggerDelegate {
    @Override
    public void error(String tag, String message) {
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Log.e(tag, message);
        }
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Log.e(tag, message);
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Log.d(tag, message);
        }
    }

    @Override
    public void info(String tag, String message) {
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Log.i(tag, message);
        }
    }
}
package ru.true_ip.trueip.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;
import ru.true_ip.trueip.di.DaggerMainComponent;
import ru.true_ip.trueip.di.MainComponent;
import ru.true_ip.trueip.di.modules.ContextModule;
import ru.true_ip.trueip.service.service.SipService;


public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static MainComponent mainComponent;
    private static Application application;
    private static Context context;
    private static SipService sipService;
    private static boolean localeChanged;
    private static int activityNumber;

    public static MainComponent getMainComponent() {
        return mainComponent;
    }

    public static boolean shouldCheckServerStatus = true;

    public static boolean isLocaleChanged() {
        return localeChanged;
    }

    public static void setLocaleChanged(boolean localeChanged) {
        App.localeChanged = localeChanged;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
        context = getApplicationContext();
        mainComponent = DaggerMainComponent.builder()
                .contextModule(new ContextModule(application = this))
                .build();
    }

    public static String getStringRes(@StringRes int stringId) {
        return application.getString(stringId);
    }

    public static Context getContext() { return context; }

    public static SipService getSipService() { return sipService; }
    public static void setSipService(SipService sipService) {
        App.sipService = sipService;
    }

    public static void incrementStartedActivityNumber() {
        activityNumber++;
    }

    public static void decrementStartedActivityNumber() {
        activityNumber--;
    }

    public static int getStartedActivityNumber() {
        return activityNumber;
    }
}

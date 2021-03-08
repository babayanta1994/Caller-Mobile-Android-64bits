package ru.true_ip.trueip.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 13-Sep-17.
 */

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}

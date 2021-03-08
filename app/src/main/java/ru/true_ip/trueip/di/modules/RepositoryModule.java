package ru.true_ip.trueip.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.true_ip.trueip.db.AppDatabase;
import ru.true_ip.trueip.db.DbSchemeMigrations;
import ru.true_ip.trueip.repository.Cache;
import ru.true_ip.trueip.repository.RepositoryController;

/**
 * Created by user on 13-Sep-17.
 *
 */

@Module(includes = CacheModule.class)
public class RepositoryModule {


    public static final String APP_DATABASE_NAME = "APP_DATABASE_NAME";

    @Provides
    @Singleton
    public AppDatabase appDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, APP_DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(DbSchemeMigrations.MIGRATIONS) // adding migration objects
                .build();
    }

    @Provides
    public RepositoryController provideRepositoryController(AppDatabase appDatabase, Cache cache) {
        return new RepositoryController(appDatabase, cache);
    }

}

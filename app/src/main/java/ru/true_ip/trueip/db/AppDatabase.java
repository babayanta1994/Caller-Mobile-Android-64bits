package ru.true_ip.trueip.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.true_ip.trueip.db.dao.DevicesDao;
import ru.true_ip.trueip.db.dao.LocksDao;
import ru.true_ip.trueip.db.dao.MessagesDao;
import ru.true_ip.trueip.db.dao.ObjectDao;
import ru.true_ip.trueip.db.dao.ServicesDao;
import ru.true_ip.trueip.db.dao.SettingsDao;
import ru.true_ip.trueip.db.dao.UserDao;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.LocksDb;
import ru.true_ip.trueip.db.entity.MessageDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.db.entity.ServicesDb;
import ru.true_ip.trueip.db.entity.SettingsDb;
import ru.true_ip.trueip.db.entity.UserDb;


/**
 * Created by Eugen on 25.09.2017.
 *
 */

@Database(entities = {
        UserDb.class,
        SettingsDb.class,
        ObjectDb.class,
        DevicesDb.class,
        LocksDb.class,
        ServicesDb.class,
        MessageDb.class}, version = 6, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static final String LOGCAT = AppDatabase.class.getSimpleName();

    public abstract ObjectDao objectDao();

    public abstract UserDao userDao();

    public abstract SettingsDao settingsDao();

    public abstract DevicesDao devicesDao();

    public abstract LocksDao locksDao();

    public abstract ServicesDao servicesDao();

    public abstract MessagesDao messagesDao();
}

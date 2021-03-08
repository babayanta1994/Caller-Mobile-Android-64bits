package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.SettingsDb;

/**
 * Created by user on 27-Sep-17.
 */

@Dao
public interface SettingsDao {

    @Query("SELECT * FROM Settings")
    Single<List<SettingsDb>> getAllSettings();

    @Query("SELECT * FROM Settings WHERE user_id = :user_id")
    Single<SettingsDb> getSettingByUserId(int user_id);

    @Query("SELECT * FROM Settings WHERE setting_id = :setting_id")
    Single<SettingsDb> getSettingBySettingsId(int setting_id);

    @Query("SELECT * FROM Settings LIMIT 1")
    Single<SettingsDb> getFirstSettings();

    @Query("SELECT COUNT(*) from Settings")
    int countSettings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SettingsDb settingsDB);

    @Delete
    void delete(SettingsDb settingsDB);

}

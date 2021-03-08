package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by user on 27-Sep-17.
 */

@Entity(tableName = "Settings")
public class SettingsDb {

    @PrimaryKey(autoGenerate = true)
    public int setting_id;

    // 1 - with video, 0 - not
    @ColumnInfo(name = "call_type")
    public int call_type;

    @ColumnInfo(name = "user_id")
    public int user_id;

    public int getSetting_id() {
        return setting_id;
    }

    public void setSetting_id(int setting_id) {
        this.setting_id = setting_id;
    }

    public int getCall_type() {
        return call_type;
    }

    public void setCall_type(int call_type) {
        this.call_type = call_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

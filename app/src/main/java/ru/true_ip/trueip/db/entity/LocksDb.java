package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by user on 04-Oct-17.
 */

@Entity(tableName = "Locks")
public class LocksDb {

    @PrimaryKey(autoGenerate = true)
    public int lock_id;

    @ColumnInfo(name = "dtmf_command")
    public String dtmf_command;

    @ColumnInfo(name = "device_id")
    public int device_id;

    public int getLock_id() {
        return lock_id;
    }

    public void setLock_id(int lock_id) {
        this.lock_id = lock_id;
    }

    public String getDtmf_command() {
        return dtmf_command;
    }

    public void setDtmf_command(String dtmf_command) {
        this.dtmf_command = dtmf_command;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }
}

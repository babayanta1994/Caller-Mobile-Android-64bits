package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by user on 04-Oct-17.
 *
 */

@Entity(tableName = "Services")
public class ServicesDb {

    @PrimaryKey(autoGenerate = true)
    public int service_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "object_id")
    public int object_id;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }
}

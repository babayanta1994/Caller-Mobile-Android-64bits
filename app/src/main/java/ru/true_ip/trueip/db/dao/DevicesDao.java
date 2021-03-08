package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.DevicesDb;

/**
 * Created by user on 27-Sep-17.
 *
 */

@Dao
public interface DevicesDao {

    @Query("SELECT * FROM Devices")
    Single<List<DevicesDb>> getAllDevices();

    @Query("SELECT * FROM Devices WHERE device_type = :device_type AND object_id = :object_id")
    Single<List<DevicesDb>> getAllDevicesByType(int device_type, int object_id);

    @Query("SELECT * FROM Devices WHERE object_id = :object_id")
    Single<List<DevicesDb>> getAllDevicesByObjectId(int object_id);

    @Query("SELECT * FROM Devices WHERE device_id = :device_id")
    Single<DevicesDb> getDeviceByDeviceId(int device_id);

    @Query("SELECT * FROM Devices WHERE device_server_id = :serverId AND device_type = :deviceType AND object_id = :objectId")
    Single<DevicesDb> getDevicesOfObjectByTypeAndServerId(int objectId, int serverId, int deviceType);

    @Query("SELECT * FROM Devices WHERE device_type = :deviceType AND object_id = :objectId")
    Single<List<DevicesDb>> getDevicesOfObjectByType(int objectId, int deviceType);

    @Query("SELECT * FROM Devices WHERE is_favorite = 1")
    Single<List<DevicesDb>> getFavoriteDevices();

    @Query("SELECT COUNT(*) from Devices")
    int countDevices();

    @Query("SELECT COUNT(*) from Devices WHERE device_id = :deviceId")
    int countDevices(int deviceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DevicesDb devicesDb);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DevicesDb> devicesDbs);

    @Update
    void update(DevicesDb devicesDb);

    @Delete
    void delete(DevicesDb... devicesDb);

    @Delete
    void delete(List<DevicesDb> devicesDbs);

    @Query("DELETE FROM Devices WHERE object_id = :object_id")
    int removeDevicesByObjectId(int object_id);
}

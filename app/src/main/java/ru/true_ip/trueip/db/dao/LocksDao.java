package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.LocksDb;

/**
 * Created by user on 04-Oct-17.
 */
@Dao
public interface LocksDao {

    @Query("SELECT * FROM Locks")
    Single<List<LocksDb>> getAllLocks();

    @Query("SELECT * FROM Locks WHERE lock_id = :lock_id")
    Single<LocksDb> getLockByLockId(int lock_id);

    @Query("SELECT * FROM Locks WHERE device_id = :device_id")
    Single<LocksDb> getLockByDeviceId(int device_id);

    @Query("SELECT * FROM Locks LIMIT 1")
    Single<LocksDb> getFirstLock();

    @Query("SELECT COUNT(*) from Locks")
    int countLocks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocksDb locksDb);

    @Delete
    void delete(LocksDb locksDb);

}

package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.ServicesDb;

/**
 * Created by user on 04-Oct-17.
 */

@Dao
public interface ServicesDao {

    @Query("SELECT * FROM Services")
    Single<List<ServicesDb>> getAllServices();

    @Query("SELECT * FROM Services WHERE service_id = :service_id")
    Single<ServicesDb> getServiceByServiceId(int service_id);

    @Query("SELECT * FROM Services WHERE object_id = :object_id")
    Single<ServicesDb> getServiceByObjectId(int object_id);

    @Query("SELECT * FROM Services LIMIT 1")
    Single<ServicesDb> getFirstService();

    @Query("SELECT COUNT(*) FROM Services")
    int countServices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServicesDb servicesDb);

    @Delete
    void delete(ServicesDb servicesDb);
}

package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.ObjectDb;


/**
 * Created by Eugen on 25.09.2017.
 */
@Dao
public interface ObjectDao {

    @Query("SELECT * FROM Objects")
    Single<List<ObjectDb>> getAllObjects();

    @Query("SELECT * FROM Objects WHERE is_cloud = 1")
    Single<List<ObjectDb>> getHLMObjects();

    @Query("SELECT COUNT(*) FROM Objects WHERE is_cloud = 1")
    Integer countHLMObjects();

    @Query("SELECT COUNT(*) FROM Objects WHERE flat_number = :flat_number AND activation_code = :activation_code")
    Integer countSameObject(String flat_number, String activation_code);

    @Query("SELECT * FROM Objects WHERE is_cloud = 0")
    Single<List<ObjectDb>> getLocalObjects();

    @Query("SELECT * FROM Objects WHERE object_id = :object_id")
    Single<ObjectDb> getObject(int object_id);

    @Query("SELECT * FROM Objects WHERE user_id = :user_id")
    Single<ObjectDb> getObjectByUserId(Integer user_id);

    @Query("SELECT COUNT(*) from Objects")
    Integer countObjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ObjectDb... objectDbs);

    @Query("UPDATE Objects SET is_object_active =:value WHERE object_id = :objectId")
    void setObjectActive(int objectId, Integer value);

    @Update
    void update(ObjectDb objectDb);

    @Delete
    void delete(ObjectDb objectDb);
}

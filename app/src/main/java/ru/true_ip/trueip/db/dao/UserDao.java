package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.UserDb;

/**
 * Created by user on 26-Sep-17.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM Users")
    Single<List<UserDb>> getAllUsers();

    @Query("SELECT * FROM Users WHERE user_id = :user_id")
    Single<UserDb> getUserById(int user_id);

    @Query("SELECT * FROM Users LIMIT 1")
    Single<UserDb> getFirstUser();

    @Query("SELECT COUNT(*) from Users")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserDb userDb);

    @Update
    void updateUser(UserDb userDb);

    @Delete
    void delete(UserDb userDb);

}

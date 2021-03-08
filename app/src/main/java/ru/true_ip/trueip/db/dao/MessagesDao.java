package ru.true_ip.trueip.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.true_ip.trueip.db.entity.MessageDb;

/**
 * Created by ektitarev on 21/01/2019.
 *
 */

@Dao
public interface MessagesDao {

    @Query("select * from Messages order by created_at asc")
    Single<List<MessageDb>> getAllMessages();

    @Query("select * from Messages where review_id = :commentId order by created_at asc")
    Single<List<MessageDb>> getAllMessagesByCommentId(int commentId);

    @Query("select * from Messages order by created_at asc limit :rowsCount offset :offset")
    Single<List<MessageDb>> getMessagesRange(int rowsCount, int offset);

    @Query("select * from Messages where id = :id")
    Single<List<MessageDb>> getMessageById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<MessageDb> messages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MessageDb messages);

    @Query("update Messages set answers_count = :numberOfMessages where review_id = :dialogId")
    void updateAnswersCount(int dialogId, int numberOfMessages);

    @Query("delete from Messages")
    void deleteAllMessages();

    @Query("delete from Messages where user_id = :userId")
    void deleteMessagesByUserId(int userId);
}

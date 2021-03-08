package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.true_ip.trueip.models.responses.AnswerModel;
import ru.true_ip.trueip.utils.Utils;

/**
 * Created by ektitarev on 21/01/2019.
 *
 */

@Entity(tableName = "Messages")
public class MessageDb {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "review_id")
    public Integer reviewId;

    @ColumnInfo(name = "user_id")
    public Integer userId;

    @ColumnInfo(name = "is_concierge")
    public Integer isConsierge;

    @ColumnInfo(name = "is_viewed")
    public Integer isViewed;

    @ColumnInfo(name = "apartment")
    public String apartment;

    @ColumnInfo(name = "msg_text")
    public String text;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "updated_at")
    public String updatedAt;

    @ColumnInfo(name = "answers_count")
    private Integer answersCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsConsierge() {
        return isConsierge;
    }

    public void setIsConsierge(Integer isConsierge) {
        this.isConsierge = isConsierge;
    }

    public Integer getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(Integer isViewed) {
        this.isViewed = isViewed;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(Integer answersCount) {
        this.answersCount = answersCount;
    }

    public static MessageDb answerModelToModelDb(AnswerModel messageModel, int answersCount) {
        MessageDb messageDb = new MessageDb();

        messageDb.setId(messageModel.getId());
        messageDb.setApartment(messageModel.getApartment());
        messageDb.setIsConsierge(messageModel.getIsConsierge());
        messageDb.setIsViewed(messageModel.getIsViewed());
        messageDb.setReviewId(messageModel.getReviewId());
        messageDb.setUserId(messageModel.getUserId());
        messageDb.setText(messageModel.getText());
        messageDb.setAnswersCount(answersCount);

        try {
            messageDb.setCreatedAt(Utils.dateToTimestamp(messageModel.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            messageDb.setCreatedAt("");
            e.printStackTrace();
        }

        try {
            messageDb.setUpdatedAt(Utils.dateToTimestamp(messageModel.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            messageDb.setUpdatedAt("");
            e.printStackTrace();
        }

        return messageDb;
    }

    public static List<MessageDb> answerModelsToModelDbs (List<AnswerModel> answerModels, int answersCount) {
        List<MessageDb> messageDbs = new ArrayList<>();

        for (AnswerModel answerModel : answerModels) {
            MessageDb messageDb = answerModelToModelDb(answerModel, answersCount);
            messageDbs.add(messageDb);
        }

        return messageDbs;
    }
}

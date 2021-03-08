package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.true_ip.trueip.db.entity.MessageDb;
import ru.true_ip.trueip.utils.Utils;

/**
 * Created by ektitarev on 21/01/2019.
 *
 */

public class AnswerModel implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("review_id")
    @Expose
    public Integer reviewId;

    @SerializedName("user_id")
    @Expose
    public Integer userId;

    @SerializedName("is_concierge")
    @Expose
    public Integer isConsierge;

    @SerializedName("is_viewed")
    @Expose
    public Integer isViewed;

    @SerializedName("apartment")
    @Expose
    public String apartment;

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public AnswerModel() {}

    protected AnswerModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            reviewId = null;
        } else {
            reviewId = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        if (in.readByte() == 0) {
            isConsierge = null;
        } else {
            isConsierge = in.readInt();
        }
        if (in.readByte() == 0) {
            isViewed = null;
        } else {
            isViewed = in.readInt();
        }
        apartment = in.readString();
        text = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (reviewId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reviewId);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        if (isConsierge == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isConsierge);
        }
        if (isViewed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isViewed);
        }
        dest.writeString(apartment);
        dest.writeString(text);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnswerModel> CREATOR = new Creator<AnswerModel>() {
        @Override
        public AnswerModel createFromParcel(Parcel in) {
            return new AnswerModel(in);
        }

        @Override
        public AnswerModel[] newArray(int size) {
            return new AnswerModel[size];
        }
    };

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

    public static AnswerModel messageDbToAnswerModel(MessageDb messageDb) {
        AnswerModel answerModel = new AnswerModel();

        answerModel.setId(messageDb.getId());
        answerModel.setText(messageDb.getText());
        answerModel.setReviewId(messageDb.getReviewId());
        answerModel.setApartment(messageDb.getApartment());
        answerModel.setIsViewed(messageDb.getIsViewed());
        answerModel.setIsConsierge(messageDb.getIsConsierge());
        answerModel.setUserId(messageDb.getUserId());
        answerModel.setCreatedAt(Utils.timestampToDate(messageDb.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
        answerModel.setUpdatedAt(Utils.timestampToDate(messageDb.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));

        return answerModel;
    }

    public static List<AnswerModel> messageDbsToAnswerModels (List<MessageDb> messageDbs) {
        List<AnswerModel> answerModels = new ArrayList<>();

        for (MessageDb messageDb : messageDbs) {
            AnswerModel answerModel = messageDbToAnswerModel(messageDb);
            answerModels.add(answerModel);
        }

        return answerModels;
    }
}

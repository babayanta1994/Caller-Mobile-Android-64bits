package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentModel implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("apartment")
    @Expose
    public String apartment;
    @SerializedName("theme")
    @Expose
    public String theme;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("is_viewed")
    @Expose
    public Integer isViewed;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("answer")
    @Expose
    public String answer;
    @SerializedName("updated_at_answer")
    @Expose
    public String updatedAtAnswer;
    @SerializedName("answers_count")
    @Expose
    public Integer answersCount;

    protected CommentModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        apartment = in.readString();
        theme = in.readString();
        text = in.readString();
        if (in.readByte() == 0) {
            isViewed = null;
        } else {
            isViewed = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        answer = in.readString();
        updatedAtAnswer = in.readString();
        if (in.readByte() == 0) {
            answersCount = null;
        } else {
            answersCount = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(apartment);
        dest.writeString(theme);
        dest.writeString(text);
        if (isViewed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isViewed);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(answer);
        dest.writeString(updatedAtAnswer);
        if (answersCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(answersCount);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getIsViewed() { return isViewed; }
    public void setIsViewed(Integer isViewed) { this.isViewed = isViewed; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUpdatedAtAnswer() {
        return updatedAtAnswer;
    }
    public void setUpdatedAtAnswer(String updatedAtAnswer) {
        this.updatedAtAnswer = updatedAtAnswer;
    }
    public Integer getAnswersCount() {
        return answersCount;
    }
    public void setAnswersCount(Integer answersCount) {
        this.answersCount = answersCount;
    }
}

package ru.true_ip.trueip.models.requests;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ektitarev on 21/01/2019.
 *
 */

public class MessageModel implements Parcelable{

    @SerializedName("review_id")
    @Expose
    public Integer reviewId;

    @SerializedName("text")
    @Expose
    public String text;

    public MessageModel() {}

    protected MessageModel(Parcel in) {
        if (in.readByte() == 0) {
            reviewId = null;
        } else {
            reviewId = in.readInt();
        }
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (reviewId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reviewId);
        }
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

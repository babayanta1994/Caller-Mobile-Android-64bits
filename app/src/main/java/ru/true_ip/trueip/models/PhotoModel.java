package ru.true_ip.trueip.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ektitarev on 19.12.2017.
 */

public class PhotoModel implements Parcelable {

    public String path;
    public String timestamp;
    public Boolean removable;

    public PhotoModel(String path, long lastModifiedDate) {
        this.path = path;

        Date date = new Date(lastModifiedDate);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        timestamp = format.format(date);
    }

    protected PhotoModel(Parcel in) {
        path = in.readString();
        timestamp = in.readString();
        byte tmpRemovable = in.readByte();
        removable = tmpRemovable == 0 ? null : tmpRemovable == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(timestamp);
        dest.writeByte((byte) (removable == null ? 0 : removable ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };
}

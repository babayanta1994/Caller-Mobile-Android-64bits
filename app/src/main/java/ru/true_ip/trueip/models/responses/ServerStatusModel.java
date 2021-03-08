package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ektitarev on 04.09.2018.
 */

public class ServerStatusModel implements Parcelable {

    private int is_active;

    private String license;

    public ServerStatusModel() {}

    protected ServerStatusModel(Parcel in) {
        is_active = in.readInt();
        license = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(is_active);
        dest.writeString(license);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServerStatusModel> CREATOR = new Creator<ServerStatusModel>() {
        @Override
        public ServerStatusModel createFromParcel(Parcel in) {
            return new ServerStatusModel(in);
        }

        @Override
        public ServerStatusModel[] newArray(int size) {
            return new ServerStatusModel[size];
        }
    };

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}

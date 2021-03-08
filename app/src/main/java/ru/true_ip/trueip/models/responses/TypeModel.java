package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeModel implements Parcelable {
    /**
     * id : 766
     * text : Eveniet impedit maiores quis.
     */

    private int id;
    private String text;

    public TypeModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    protected TypeModel(Parcel in) {
        id = in.readInt();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TypeModel> CREATOR = new Creator<TypeModel>() {
        @Override
        public TypeModel createFromParcel(Parcel in) {
            return new TypeModel(in);
        }

        @Override
        public TypeModel[] newArray(int size) {
            return new TypeModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
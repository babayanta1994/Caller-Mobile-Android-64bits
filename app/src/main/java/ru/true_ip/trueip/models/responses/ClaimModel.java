package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ClaimModel implements Parcelable {

    /**
     * id : 123
     * created_at : 1980-07-19 00:18:13
     * need_at : 1974-10-23 02:51:41
     * type : {"id":766,"text":"Eveniet impedit maiores quis."}
     * phone : +6740858343692
     * status : Accepted
     */

    public static class PhotoUrl implements Parcelable {
        private String image_url;

        protected PhotoUrl(Parcel in) {
            image_url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(image_url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PhotoUrl> CREATOR = new Creator<PhotoUrl>() {
            @Override
            public PhotoUrl createFromParcel(Parcel in) {
                return new PhotoUrl(in);
            }

            @Override
            public PhotoUrl[] newArray(int size) {
                return new PhotoUrl[size];
            }
        };

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }

    private String id;
    private String created_at;
    private String need_at;
    private TypeModel type;
    private String phone;
    private String status;
    private String claim_text;
    private List<PhotoUrl> images;

    public ClaimModel() {}

    protected ClaimModel(Parcel in) {
        id = in.readString();
        created_at = in.readString();
        need_at = in.readString();
        type = in.readParcelable(TypeModel.class.getClassLoader());
        phone = in.readString();
        status = in.readString();
        claim_text = in.readString();
        images = in.createTypedArrayList(PhotoUrl.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(created_at);
        dest.writeString(need_at);
        dest.writeParcelable(type, flags);
        dest.writeString(phone);
        dest.writeString(status);
        dest.writeString(claim_text);
        dest.writeTypedList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClaimModel> CREATOR = new Creator<ClaimModel>() {
        @Override
        public ClaimModel createFromParcel(Parcel in) {
            return new ClaimModel(in);
        }

        @Override
        public ClaimModel[] newArray(int size) {
            return new ClaimModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNeed_at() {
        return need_at;
    }

    public void setNeed_at(String need_at) {
        this.need_at = need_at;
    }

    public TypeModel getType() {
        return type;
    }

    public void setType(TypeModel type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() { return status; }

    public void setClaim_text(String claim_text) { this.claim_text = claim_text; }

    public String getClaim_text() { return claim_text; }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PhotoUrl> getImages() {
        return images;
    }

    public void setImages(List<PhotoUrl> images) {
        this.images = images;
    }
}
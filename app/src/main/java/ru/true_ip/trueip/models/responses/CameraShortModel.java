package ru.true_ip.trueip.models.responses;

/**
 * Created by afilimonov on 22.12.2017.
 */

public class CameraShortModel {
    public Integer id;
    public String name;
    public String rtsp_link;
    public String createdAt;
    public String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRtsp_link() {
        return rtsp_link;
    }

    public void setRtsp_link(String rtsp_link) {
        this.rtsp_link = rtsp_link;
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
}

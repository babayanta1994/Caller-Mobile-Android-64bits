package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 22.09.2017.
 */

public class CamerasListResponse {

    private List<CameraModel> cameras;

    public List<CameraModel> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraModel> cameras) {
        this.cameras = cameras;
    }
}

package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 06.09.2017.
 */

public class AdvertsListModel {

    List<AdvertModel> adverts;

    public List<AdvertModel> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<AdvertModel> adverts) {
        this.adverts = adverts;
    }
}

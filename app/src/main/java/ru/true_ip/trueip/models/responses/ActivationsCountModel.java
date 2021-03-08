package ru.true_ip.trueip.models.responses;

/**
 * Created by ektitarev on 25.07.2018.
 */

public class ActivationsCountModel {
    private Integer limit_of_activations;
    private Integer number_of_activations;

    public Integer getLimit_of_activations() {
        return limit_of_activations;
    }

    public void setLimit_of_activations(Integer limit_of_activations) {
        this.limit_of_activations = limit_of_activations;
    }

    public Integer getNumber_of_activations() {
        return number_of_activations;
    }

    public void setNumber_of_activations(Integer number_of_activations) {
        this.number_of_activations = number_of_activations;
    }
}

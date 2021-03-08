package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 06.09.2017.
 */

public class ApprtmentModel {

    /**
     * apartment_number : 123
     * activation_code : 654321
     */

    private String apartment_number;
    private String activation_code;

    public String getApartment_number() {
        return apartment_number;
    }

    public void setApartment_number(String apartment_number) {
        this.apartment_number = apartment_number;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }
}

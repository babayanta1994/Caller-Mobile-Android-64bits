package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 22.09.2017.
 */

public class ClaimTypesResponse {

    private List<TypeModel> claim_types;

    public List<TypeModel> getClaimTypes() {
        return claim_types;
    }

    public void setClaimTypes(List<TypeModel> claimTypes) {
        this.claim_types = claimTypes;
    }
}

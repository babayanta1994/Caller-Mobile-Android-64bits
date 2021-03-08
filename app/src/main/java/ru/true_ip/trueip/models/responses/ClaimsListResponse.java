package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 22.09.2017.
 */

public class ClaimsListResponse {

    private List<ClaimModel> claims;

    public List<ClaimModel> getClaims() {
        return claims;
    }

    public void setClaims(List<ClaimModel> claims) {
        this.claims = claims;
    }
}

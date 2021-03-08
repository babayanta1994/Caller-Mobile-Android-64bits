package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 22.09.2017.
 */

public class ClaimResponse {

    /**
     * claim : {"id":"123","created_at":"1980-07-19 00:18:13","need_at":"1974-10-23 02:51:41","type":{"id":766,"text":"Eveniet impedit maiores quis."},"phone":"+6740858343692","status":"Accepted"}
     */

    private ClaimModel claim;

    public ClaimModel getClaim() {
        return claim;
    }

    public void setClaim(ClaimModel claim) {
        this.claim = claim;
    }
}

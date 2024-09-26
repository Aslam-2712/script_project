package com.example.attempt2.model.redemptionpojo;

import org.springframework.stereotype.Component;

@Component
public class RedemptionAttributes {

    private String force_redemption_type;


    private String force_message;


    private String requested_punches;

   /* public RedemptionAttributes(String forceRedemptionType, String forceMessage, int requestedPunches) {
        this.force_redemption_type=forceRedemptionType;
        this.force_message=forceMessage;
        this.requested_punches=requestedPunches;
    }*/


    // Getters and setters
    public String getForce_redemption_type() {
        return force_redemption_type;
    }

    public void setForce_redemption_type(String force_redemption_type) {
        this.force_redemption_type = force_redemption_type;
    }

    public String getForce_message() {
        return force_message;
    }

    public void setForce_message(String force_message) {
        this.force_message = force_message;
    }

    public String getRequested_punches() {
        return requested_punches;
    }

    public void setRequested_punches(String requested_punches) {
        this.requested_punches = requested_punches;
    }
}

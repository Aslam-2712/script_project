package com.example.attempt2.model.redemptionpojo;

import org.springframework.stereotype.Component;

@Component

public class CompleteData {

   // private RedemptionAttributes redemptionAttributes;

    private String user_id;
    private RedemptionAttributes redemption;

    // Constructors, getters, and setters


 /*   public CompleteData(int user_id, RedemptionAttributes redemption) {
        this.user_id = user_id;
        this.redemption = redemption;
    }
*/
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public RedemptionAttributes getRedemption() {
        return redemption;
    }

    public void setRedemption(RedemptionAttributes redemption) {
        this.redemption = redemption;
    }
}

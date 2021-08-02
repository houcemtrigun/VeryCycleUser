package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditCardModel {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("card_number")
        @Expose
        public String cardNumber;
        @SerializedName("expiry_date")
        @Expose
        public String expiryDate;
        @SerializedName("expiry_month")
        @Expose
        public String expiryMonth;
        @SerializedName("cvc_code")
        @Expose
        public String cvcCode;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("card_holder_name")
        @Expose
        public String cardHolderName;



    }

}




package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
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
        @SerializedName("provider_id")
        @Expose
        public String providerId;
        @SerializedName("problem")
        @Expose
        public String problem;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time")
        @Expose
        public String time;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("cycle_image")
        @Expose
        public String cycleImage;
        @SerializedName("repaire_image")
        @Expose
        public String repaireImage;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("cycle_id")
        @Expose
        public String cycleId;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("provider_details")
        @Expose
        public ProviderDetails providerDetails;

        public class ProviderDetails {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("username")
            @Expose
            public String username;
            @SerializedName("email")
            @Expose
            public String email;
            @SerializedName("password")
            @Expose
            public String password;
            @SerializedName("mobile")
            @Expose
            public String mobile;
            @SerializedName("image")
            @Expose
            public String image;
            @SerializedName("address")
            @Expose
            public String address;
            @SerializedName("lat")
            @Expose
            public String lat;
            @SerializedName("lon")
            @Expose
            public String lon;
            @SerializedName("register_id")
            @Expose
            public String registerId;
            @SerializedName("type")
            @Expose
            public String type;
            @SerializedName("status")
            @Expose
            public String status;
            @SerializedName("email_code")
            @Expose
            public String emailCode;
            @SerializedName("date_time")
            @Expose
            public String dateTime;
            @SerializedName("passkey")
            @Expose
            public String passkey;
            @SerializedName("amount")
            @Expose
            public String amount;
            @SerializedName("km")
            @Expose
            public String km;
            @SerializedName("country_code")
            @Expose
            public String countryCode;



        }

    }

}










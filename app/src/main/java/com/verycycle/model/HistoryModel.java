package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryModel {

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
        @SerializedName("arrived_time")
        @Expose
        public String arrivedTime;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("finish_time")
        @Expose
        public String finishTime;
        @SerializedName("booking_image")
        @Expose
        public String bookingImage;
        @SerializedName("extra_amount")
        @Expose
        public String extraAmount;
        @SerializedName("total_amount")
        @Expose
        public Integer totalAmount;
        @SerializedName("service_amount")
        @Expose
        public String serviceAmount;



    }

}





package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSummaryModel {

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
        @SerializedName("provider_id")
        @Expose
        public String providerId;
        @SerializedName("manual_status")
        @Expose
        public String manualStatus;
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
        @SerializedName("booktype")
        @Expose
        public String booktype;
        @SerializedName("payment_status")
        @Expose
        public String paymentStatus;
        @SerializedName("send_privders")
        @Expose
        public String sendPrivders;
        @SerializedName("accept_time_slote")
        @Expose
        public String acceptTimeSlote;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("manual_service")
        @Expose
        public String manualService;
        @SerializedName("manual_desc")
        @Expose
        public String manualDesc;
        @SerializedName("manual_amount")
        @Expose
        public String manualAmount;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("service_amount")
        @Expose
        public String serviceAmount;



    }

}



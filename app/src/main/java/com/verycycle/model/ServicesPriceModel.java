package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ServicesPriceModel {

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
        @SerializedName("service_id")
        @Expose
        public String serviceId;
        @SerializedName("price_for")
        @Expose
        public String priceFor;
        @SerializedName("price_for_fr")
        @Expose
        public String priceForFr;
        @SerializedName("from_price")
        @Expose
        public String fromPrice;
        @SerializedName("to_price")
        @Expose
        public String toPrice;

        @SerializedName("chk")
        @Expose
        public boolean chk;


        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }

}
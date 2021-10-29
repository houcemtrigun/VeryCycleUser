package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class SubProblmModel {

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
        @SerializedName("problem_details_id")
        @Expose
        public String problemDetailsId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("problem_details_name")
        @Expose
        public String problemDetailsName;
        @SerializedName("price")
        @Expose
        public String price;

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
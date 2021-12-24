package com.verycycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ServicesModel {

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
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("name_fr")
        @Expose
        public String nameFr;
        @SerializedName("problem_id")
        @Expose
        public String problemId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("description_fr")
        @Expose
        public String descriptionFr;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("problem_details_name")
        @Expose
        public String problemDetailsName;

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
package com.verycycle.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProblemModel {

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
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("price")
        @Expose
        public String price;




        @SerializedName("chk")
        @Expose
        public boolean chk = false;

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }

}



package com.verycycle.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateTimeModel {

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

        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time_slot")
        @Expose
        public List<TimeSlot> timeSlot = null;

        public class TimeSlot {

            @SerializedName("time")
            @Expose
            public String time;
            @SerializedName("available")
            @Expose
            public String available;


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

}



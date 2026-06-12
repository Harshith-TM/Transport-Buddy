package com.example.transportbuddy;

public class busTicketDetails {
    String bt_Id;
    String bt_time;
    String bt_date;
    String bt_busNum;
    String bt_fromPlace;
    String bt_toPlace;
    String bt_count;
    String bt_amount;
    String mode;

    public  busTicketDetails()
    {}

    public busTicketDetails(String bt_Id,String bt_time, String bt_date, String bt_busNum,String bt_fromPlace,String bt_toPlace,String bt_count,String bt_amount,String mode) {
        this.bt_Id = bt_Id;
        this.bt_time = bt_time;
        this.bt_date = bt_date;
        this.bt_busNum = bt_busNum;
        this.bt_fromPlace = bt_fromPlace;
        this.bt_toPlace = bt_toPlace;
        this.bt_count = bt_count;
        this.bt_amount = bt_amount;
        this.mode=mode;
    }

    public String getBT_Id() {
        return bt_Id;
    }
    public String getBT_time() {
        return bt_time;
    }
    public String getBT_date() {
        return bt_date;
    }
    public String getBT_busNum() {
        return bt_busNum;
    }
    public String getBT_fromPlace(){return bt_fromPlace;}
    public String getBT_toPlace(){return bt_toPlace;}
    public String getBT_count(){return bt_count;}
    public String getBT_amount(){return bt_amount;}
    public String getMode(){return mode;}
}

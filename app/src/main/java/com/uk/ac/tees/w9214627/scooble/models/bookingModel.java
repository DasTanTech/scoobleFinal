package com.uk.ac.tees.w9214627.scooble.models;

public class bookingModel {
    private String BID,UID,AID,CAR_COMPANY,CAR_MODEL,CAR_PICURL,CAR_PICKPLACE,CAR_PICKDATE;

    public bookingModel(String BID, String CAR_COMPANY, String CAR_MODEL, String CAR_PICURL, String CAR_PICKPLACE, String CAR_PICKDATE,String uid,String aid) {
        this.BID = BID;
        this.CAR_COMPANY = CAR_COMPANY;
        this.CAR_MODEL = CAR_MODEL;
        this.CAR_PICURL = CAR_PICURL;
        this.CAR_PICKPLACE = CAR_PICKPLACE;
        this.CAR_PICKDATE = CAR_PICKDATE;
        this.AID = aid;
        this.UID = uid;
    }

    public bookingModel() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getCAR_COMPANY() {
        return CAR_COMPANY;
    }

    public void setCAR_COMPANY(String CAR_COMPANY) {
        this.CAR_COMPANY = CAR_COMPANY;
    }

    public String getCAR_MODEL() {
        return CAR_MODEL;
    }

    public void setCAR_MODEL(String CAR_MODEL) {
        this.CAR_MODEL = CAR_MODEL;
    }

    public String getCAR_PICURL() {
        return CAR_PICURL;
    }

    public void setCAR_PICURL(String CAR_PICURL) {
        this.CAR_PICURL = CAR_PICURL;
    }

    public String getCAR_PICKPLACE() {
        return CAR_PICKPLACE;
    }

    public void setCAR_PICKPLACE(String CAR_PICKPLACE) {
        this.CAR_PICKPLACE = CAR_PICKPLACE;
    }

    public String getCAR_PICKDATE() {
        return CAR_PICKDATE;
    }

    public void setCAR_PICKDATE(String CAR_PICKDATE) {
        this.CAR_PICKDATE = CAR_PICKDATE;
    }
}

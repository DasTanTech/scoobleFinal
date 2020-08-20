package com.uk.ac.tees.w9214627.scooble.models;

public class singleCarModel {

    private String CID,carCompany,carModel,carPicURL;

    public singleCarModel(String CID, String carCompany, String carModel, String carPicURL) {
        this.CID = CID;
        this.carCompany = carCompany;
        this.carModel = carModel;
        this.carPicURL = carPicURL;
    }

    public singleCarModel() {
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCarCompany() {
        return carCompany;
    }

    public void setCarCompany(String carCompany) {
        this.carCompany = carCompany;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPicURL() {
        return carPicURL;
    }

    public void setCarPicURL(String carPicURL) {
        this.carPicURL = carPicURL;
    }
}

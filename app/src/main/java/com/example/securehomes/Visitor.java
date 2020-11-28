package com.example.securehomes;

import java.io.Serializable;

public class Visitor implements Serializable {
    private String name,purpose,Vehicle_no;
    private String time;
    private String  telNum;
    private String toFlatNumber;
    private String imageId;
    private int img;

    public Visitor() {
    }

    public Visitor(String name, String purpose, int img) {
        this.name = name;
        this.purpose = purpose;
        this.img = img;
    }

    public Visitor(String name, String purpose, String vehicle_no, String time, String telNum, String toFlatNumber) {
        this.name = name;
        this.purpose = purpose;
        this.Vehicle_no = vehicle_no;
        this.time = time;
        this.telNum = telNum;
        this.toFlatNumber = toFlatNumber;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getToFlatNumber() {
        return toFlatNumber;
    }

    public void setToFlatNumber(String toFlatNumber) {
        this.toFlatNumber = toFlatNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getVehicle_no() {
        return Vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        Vehicle_no = vehicle_no;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


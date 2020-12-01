package com.example.securehomes;

public class OwnerModel {
    String imgUrl;
    String name;
    String phone;
    String flatNo;
    String email;

    private String key;

    public OwnerModel(String imgUrl, String name, String phone, String flatNo, String email) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.phone = phone;
        this.flatNo = flatNo;
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OwnerModel() {
    }
}

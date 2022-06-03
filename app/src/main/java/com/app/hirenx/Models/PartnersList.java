package com.app.hirenx.Models;

public class PartnersList {

    public PartnersList(String fullName, String userImage, String phoneNumber, String address) {
        this.fullName = fullName;
        this.userImage = userImage;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String fullName;
    private String userImage;
    private String phoneNumber;
    private String address;




    public PartnersList() {
    }


}

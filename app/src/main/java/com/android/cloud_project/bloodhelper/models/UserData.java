package com.android.cloud_project.bloodhelper.models;

public class UserData {

    private String name, email, contact, address;
    public String pincode;
    private int gender, bloodgroup, city;

    public UserData() {

    }

    public String getContact() {

        return contact;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public int getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public int getBloodgroup() {

        return bloodgroup;
    }

    public void setBloodgroup(int bloodgroup) {

        this.bloodgroup = bloodgroup;
    }

    public String getEmail() {

        return email;
    }

    public int getGender() {

        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setGender(int gender) {

        this.gender = gender;
    }


}

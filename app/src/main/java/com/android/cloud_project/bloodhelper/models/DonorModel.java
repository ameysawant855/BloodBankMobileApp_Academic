package com.android.cloud_project.bloodhelper.models;

public class DonorModel {
    private String Contact, Address, Name;

    public String getName() {
        return this.Name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String donor_contact) {
        this.Contact = donor_contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }
}

package com.android.cloud_project.bloodhelper.models;

import java.io.Serializable;

public class UserModel implements Serializable {
   private String Address, Contact;
   private String Name, BloodGroup;
   private String Time, Date;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        this.Contact = contact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
       this.Name = name;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public String getDate() {
        return Date;
    }
}

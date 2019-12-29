package com.example.smartchart.ModelClass;

import java.io.Serializable;

public class Users implements Serializable {
    public String firstname,lastname,id,phonenumber;
    public Users(){

    }

    public Users(String firstname, String lastname, String id, String phonenumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.phonenumber = phonenumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}

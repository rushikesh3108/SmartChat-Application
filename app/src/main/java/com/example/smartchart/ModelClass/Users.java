package com.example.smartchart.ModelClass;

import java.io.Serializable;

public class Users extends Shedulermessagedata implements Serializable {
    public String firstname,lastname,id,phonenumber;
    private String  status, ProfileImageURI;
    public Users(){

    }

/*
    public Users(String firstname, String lastname, String id, String phonenumber, String profileImageURI)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.phonenumber = phonenumber;
        ProfileImageURI = profileImageURI;
    }
*/

    public Users(String firstname, String lastname, String id, String phonenumber, String status, String profileImageURI) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.phonenumber = phonenumber;
        this.status = status;
        ProfileImageURI = profileImageURI;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/* public Users(String firstname, String lastname, String id, String phonenumber ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;

        this.phonenumber = phonenumber;
    }*/

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

    public String getProfileImageURI() {
        return ProfileImageURI;
    }

    public void setProfileImageURI(String profileImageURI) {
        ProfileImageURI = profileImageURI;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}

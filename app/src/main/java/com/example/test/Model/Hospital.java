package com.example.test.Model;

import java.io.Serializable;

public class Hospital implements Serializable {
    String name,type, bloodGroup, email, id,profilepictureurl, idNumber, search,address,city;
    public Hospital(){}
    public Hospital(String name, String bloodGroup, String email, String id, String profilepictureurl, String idNumber, String search, String type,String address,String city) {
        this.address= address;
        this.bloodGroup = bloodGroup;
        this.city=city;
        this.email = email;
        this.id = id;
        this.idNumber = idNumber;
        this.name = name;
        this.profilepictureurl = profilepictureurl;
        this.search = search;
        this.type = type;

    }
    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }
    public void getCity(String city) {
        this.city = city;
    }
}

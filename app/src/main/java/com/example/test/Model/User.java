package com.example.test.Model;

public class User {
    String name, bloodGroup, email, id,profilepictureurl, idNumber, search, type;

    public User() {
    }

    public User(String name, String bloodGroup, String email,String profilepictureurl, String id, String idNumber, String search, String type) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.id = id;
        this.idNumber = idNumber;
        this.search = search;
        this.type = type;
        this.profilepictureurl = profilepictureurl;
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
}

package com.example.test.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllHospitals implements Parcelable {
    private  String name,address,id,city,openHours,website;

    public AllHospitals() {
    }

    protected AllHospitals(Parcel in) {
        name = in.readString();
        address = in.readString();
        id = in.readString();
        city = in.readString();
        openHours = in.readString();
        website = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(id);
        dest.writeString(city);
        dest.writeString(openHours);
        dest.writeString(website);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllHospitals> CREATOR = new Creator<AllHospitals>() {
        @Override
        public AllHospitals createFromParcel(Parcel in) {
            return new AllHospitals(in);
        }

        @Override
        public AllHospitals[] newArray(int size) {
            return new AllHospitals[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

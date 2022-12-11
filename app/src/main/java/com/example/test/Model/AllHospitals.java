package com.example.test.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllHospitals implements Parcelable {
    private  String name,address,id;

    public AllHospitals() {
    }

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

    protected AllHospitals(Parcel in) {
        name = in.readString();
        address = in.readString();
        id = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(id);
    }
}

package com.example.test.Model;

public class BookingInformation {
    private  String customerName, customerPhone, time, hospitalName, hospitalId,hospitalAddress,customerId;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String hospitalName, String hospitalId, String hospitalAddress,Long slot,String customerId) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.hospitalName = hospitalName;
        this.hospitalId = hospitalId;
        this.hospitalAddress = hospitalAddress;
        this.slot= slot;
        this.customerId=customerId;
    }
    public  String getCustomerId(){
        return customerId;
    }
    public  void setCustomerId(String customerId){
        this.customerId=customerId;
    }


    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }
}

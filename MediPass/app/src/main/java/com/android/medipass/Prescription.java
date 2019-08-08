package com.android.medipass;

public class Prescription {

    private int pID, status, mID, quantity;
    private String expDate, fName, sName, mName, type;

    public Prescription(int pID, int status, int mID, int quantity, String expDate, String fName, String sName, String mName, String type) {
        this.pID = pID;
        this.status = status;
        this.mID = mID;
        this.quantity = quantity;
        this.expDate = expDate;
        this.fName = fName;
        this.sName = sName;
        this.mName = mName;
        this.type = type;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

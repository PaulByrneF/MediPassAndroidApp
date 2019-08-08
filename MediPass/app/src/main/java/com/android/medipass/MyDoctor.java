package com.android.medipass;

public class MyDoctor {

    private int mpid, cStatus,regNo, addressID;
    private String CreateAt, ExpireAt, fName, sName, email, phNo, fos;

    public MyDoctor(int mpid, int cStatus, int regNo, String createAt, String expireAt, String fName, String sName, String email, String phNo, String fos, int addressID) {
        this.mpid = mpid;
        this.cStatus = cStatus;
        this.regNo = regNo;
        CreateAt = createAt;
        ExpireAt = expireAt;
        this.fName = fName;
        this.sName = sName;
        this.email = email;
        this.phNo = phNo;
        this.fos = fos;
        this.addressID = addressID;
    }

    public int getMpid() {
        return mpid;
    }

    public void setMpid(int mpid) {
        this.mpid = mpid;
    }

    public int getcStatus() {
        return cStatus;
    }

    public void setcStatus(int cStatus) {
        this.cStatus = cStatus;
    }

    public int getRegNo() {
        return regNo;
    }

    public void setRegNo(int regNo) {
        this.regNo = regNo;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getExpireAt() {
        return ExpireAt;
    }

    public void setExpireAt(String expireAt) {
        ExpireAt = expireAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getFos() {
        return fos;
    }

    public void setFos(String fos) {
        this.fos = fos;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
}



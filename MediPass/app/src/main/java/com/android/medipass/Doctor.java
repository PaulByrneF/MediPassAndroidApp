package com.android.medipass;

public class Doctor {

    int medPractitionerID, addressID;
    String fName, sName, email, fos;

    public Doctor(int medPractitionerID, int addressID, String fName, String sName, String email, String fos) {
        this.medPractitionerID = medPractitionerID;
        this.addressID = addressID;
        this.fName = fName;
        this.sName = sName;
        this.email = email;
        this.fos = fos;
    }

    public int getMedPractitionerID() {
        return medPractitionerID;
    }

    public void setMedPractitionerID(int medPractitionerID) {
        this.medPractitionerID = medPractitionerID;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
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

    public String getFos() {
        return fos;
    }

    public void setFos(String fos) {
        this.fos = fos;
    }
}

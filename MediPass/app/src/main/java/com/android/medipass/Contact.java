package com.android.medipass;

public class Contact {

    private int contactID;
    private String phNumber, cName;

    public Contact(int contactID, String phNumber, String cName) {
        this.contactID = contactID;
        this.phNumber = phNumber;
        this.cName = cName;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}

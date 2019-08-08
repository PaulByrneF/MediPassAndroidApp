package com.android.medipass;

class MyMedicines {


    private int mID;
    private int quantity;
    private String comments;
    private String mName;
    private String purpose;
    private String type;
    private String altName;
//    private String Forms;
    private String desc;

    public MyMedicines(int mID, int quantity, String comments, String mName, String purpose, String type, String altName, String desc) {
        this.mID = mID;
        this.quantity = quantity;
        this.comments = comments;
        this.mName = mName;
        this.purpose = purpose;
        this.type = type;
        this.altName = altName;
//        Forms = forms;
        this.desc = desc;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

//    public String getForms() {
//        return Forms;
//    }
//
//    public void setForms(String forms) {
//        Forms = forms;
//    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

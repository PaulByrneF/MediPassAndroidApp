package com.android.medipass;

public class Profile {

    String dob, sex, phNo, phMobNo, bType;
    int smoker, drinker, height, weight, dnr;

    public Profile(String dob, String sex, String phNo, String phMobNo, String bType, int smoker, int drinker, int height, int weight, int dnr) {
        this.dob = dob;
        this.sex = sex;
        this.phNo = phNo;
        this.phMobNo = phMobNo;
        this.bType = bType;
        this.smoker = smoker;
        this.drinker = drinker;
        this.height = height;
        this.weight = weight;
        this.dnr = dnr;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getPhMobNo() {
        return phMobNo;
    }

    public void setPhMobNo(String phMobNo) {
        this.phMobNo = phMobNo;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public int getSmoker() {
        return smoker;
    }

    public void setSmoker(int smoker) {
        this.smoker = smoker;
    }

    public int getDrinker() {
        return drinker;
    }

    public void setDrinker(int drinker) {
        this.drinker = drinker;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDnr() {
        return dnr;
    }

    public void setDnr(int dnr) {
        this.dnr = dnr;
    }
}

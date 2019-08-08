package com.android.medipass;

import java.util.Date;

class MyConsultations {

    private int cID;
    private String diagnostics;
    private String date;
    private String mp_fname, mp_sname;
    private String c_type;

    public MyConsultations(int cID, String diagnostics, String date, String mp_fname, String mp_sname, String c_type) {
        this.cID = cID;
        this.diagnostics = diagnostics;
        this.date = date;
        this.mp_fname = mp_fname;
        this.mp_sname = mp_sname;
        this.c_type = c_type;
    }

    public int getcID() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMp_fname() {
        return mp_fname;
    }

    public void setMp_fname(String mp_fname) {
        this.mp_fname = mp_fname;
    }

    public String getMp_sname() {
        return mp_sname;
    }

    public void setMp_sname(String mp_sname) {
        this.mp_sname = mp_sname;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }
}

package com.caliber.shwaasdemo.Model;

import java.io.Serializable;

/**
 * Created by Caliber on 20-03-2018.
 */

public class Doc_Details implements Serializable{
    String DocName;
    String DocEmail;

    public String getDocHospital() {
        return DocHospital;
    }

    public void setDocHospital(String docHospital) {
        DocHospital = docHospital;
    }

    String DocHospital;
    boolean DocFirstLogin;

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    public String getDocEmail() {
        return DocEmail;
    }

    public void setDocEmail(String docEmail) {
        DocEmail = docEmail;
    }

    public boolean isDocFirstLogin() {
        return DocFirstLogin;
    }

    public void setDocFirstLogin(boolean docFirstLogin) {
        DocFirstLogin = docFirstLogin;
    }
}

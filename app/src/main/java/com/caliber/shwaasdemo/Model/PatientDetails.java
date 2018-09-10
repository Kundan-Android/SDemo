package com.caliber.shwaasdemo.Model;

/**
 * Created by Caliber on 06-03-2018.
 */

public class PatientDetails {
    private String mPatientname, mPatientID, mGender,mAge, mSmoker;
    private int  mPatientReportID;
    private double R6,R12,R20,R30,X6,X12,X20,X30,Freq;
    private boolean result;

    public int getmPatientReportID() {
        return mPatientReportID;
    }

    public void setmPatientReportID(int mPatientReportID) {
        this.mPatientReportID = mPatientReportID;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getmPatientname() {
        return mPatientname;
    }

    public void setmPatientname(String mPatientname) {
        this.mPatientname = mPatientname;
    }

    public String getmPatientID() {
        return mPatientID;
    }

    public void setmPatientID(String mPatientID) {
        this.mPatientID = mPatientID;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmSmoker() {
        return mSmoker;
    }

    public void setmSmoker(String mSmoker) {
        this.mSmoker = mSmoker;
    }

    public double getR6() {
        return R6;
    }

    public void setR6(double r6) {
        R6 = r6;
    }

    public double getR12() {
        return R12;
    }

    public void setR12(double r12) {
        R12 = r12;
    }

    public double getR20() {
        return R20;
    }

    public void setR20(double r20) {
        R20 = r20;
    }

    public double getX6() {
        return X6;
    }

    public void setX6(double x6) {
        X6 = x6;
    }

    public double getX12() {
        return X12;
    }

    public void setX12(double x12) {
        X12 = x12;
    }

    public double getX20() {
        return X20;
    }

    public void setX20(double x20) {
        X20 = x20;
    }

    public double getR30() {
        return R30;
    }

    public void setR30(double r30) {
        R30 = r30;
    }

    public double getX30() {
        return X30;
    }

    public void setX30(double x30) {
        X30 = x30;
    }

    public double getFreq() {
        return Freq;
    }

    public void setFreq(double freq) {
        Freq = freq;
    }
}

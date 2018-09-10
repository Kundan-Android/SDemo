package com.caliber.shwaasdemo.Model;

/**
 * Created by Caliber on 18-01-2018.
 */

public class Patient {
    private String name, gender,birthYear,createdOn;
    private int patientId;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Patient(){
    }
    public Patient(String name, String gender, String birthYear,int pId){
        this.name        =  name;
        this.gender      = gender;
        this.birthYear   = birthYear;
        this.patientId   = pId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}

package com.example.mobileappproject;

// this is my model class
public class Patient {
    private String pID;
    private String name;
    private String dob;
    private String age;
    private String email;
    private String password;
    private String insurancename;
    private String gpId;

    public Patient(){

    }

    public Patient(String patientId, String patientName, String patientDOB, String patientAge, String patientEmail, String password, String insuranceName, String gpId) {
        pID = patientId;
        name = patientName;
        dob = patientDOB;
        age = patientAge;
        email = patientEmail;
        this.password = password;
        insurancename = insuranceName;
        this.gpId = gpId;

    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInsurancename() {
        return insurancename;
    }

    public void setInsurancename(String insurancename) {
        this.insurancename = insurancename;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }
}

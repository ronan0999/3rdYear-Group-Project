package com.example.mediaiapp;

public class heartrisk {

    private String age;
    private String gender;
    private String chestpain;
    private String bloodpressure;
    private String cholesterol;

    public heartrisk() {
    }

    public heartrisk(String age, String gender, String chestpain , String bloodpressure , String cholesterol) {

        this.age = age;
        this.gender=gender;
        this.chestpain = chestpain;
        this.bloodpressure = bloodpressure;
        this.cholesterol = cholesterol;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChestpain() {
        return chestpain;
    }

    public void setChestpain(String chestpain) {
        this.chestpain = chestpain;
    }

    public String getBloodpressure() {
        return bloodpressure;
    }

    public void setBloodpressure(String bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }
}

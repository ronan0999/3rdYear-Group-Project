package com.example.mediaiapp;

public class diabetes {

    private String bloodpressure;
    private String skinfold;
    private String insulin;
    private String bmi;
    private String age;

    public diabetes() {
    }

    public diabetes(String insulin, String bloodpressure, String skinfold,String bmi,String age) {
        this.age=age;
        this.bloodpressure= bloodpressure;
        this.bmi=bmi;
        this.skinfold = skinfold;
        this.insulin = insulin;
    }

    public String getBloodpressure() {
        return bloodpressure;
    }

    public void setBloodpressure(String bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public String getSkinfold() {
        return skinfold;
    }

    public void setSkinfold(String skinfold) {
        this.skinfold = skinfold;
    }

    public String getInsulin() {
        return insulin;
    }

    public void setInsulin(String insulin) {
        this.insulin = insulin;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

package com.example.anshu.medstore;

public class Medicine {

    private int id;
    private String medName, medLink, medPrice;

    public Medicine(int id, String medName, String medLink, String medPrice) {
        this.id = id;
        this.medName = medName;
        this.medLink = medLink;
        this.medPrice = medPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedLink() {
        return medLink;
    }

    public void setMedLink(String medLink) {
        this.medLink = medLink;
    }

    public String getMedPrice() {
        return medPrice;
    }

    public void setMedPrice(String medPrice) {
        this.medPrice = medPrice;
    }
}


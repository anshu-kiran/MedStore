package com.example.anshu.medstore;

/**
 * Created by Anshu on 6/20/2017.
 */

public class Product {

    int id;
    String name;
    String price;

    public Product(){}

    public Product(int id, String name, String price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, String price){
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}


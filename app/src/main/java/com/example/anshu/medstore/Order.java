package com.example.anshu.medstore;

/**
 * Created by Anshu on 6/22/2017.
 */

public class Order {
    private int id, cost;
    private String username, orders;

    public Order(int id, String username, String orders, int cost) {
        this.id = id;
        this.username = username;
        this.orders = orders;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}

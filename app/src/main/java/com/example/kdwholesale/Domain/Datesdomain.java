package com.example.kdwholesale.Domain;

public class Datesdomain {
    String date;
    int orderid;

    public Datesdomain(String date, int orderid) {
        this.date = date;
        this.orderid = orderid;
    }

    public Datesdomain(){}

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

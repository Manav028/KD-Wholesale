package com.example.kdwholesale.Domain;

public class Orderitemsummary {
    int id,productQuantity;
    double price,totalPrice;
    String imagePath,ProductTitle;

    public Orderitemsummary(int id, int productQuantity, double price, double totalPrice, String imagePath, String productTitle) {
        this.id = id;
        this.productQuantity = productQuantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.imagePath = imagePath;
        ProductTitle = productTitle;
    }

    public Orderitemsummary(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }
}

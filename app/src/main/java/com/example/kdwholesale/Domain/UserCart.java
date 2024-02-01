package com.example.kdwholesale.Domain;

import java.io.Serializable;

public class UserCart implements Serializable {
    private String ProductTitle,ImagePath;
    private int ID,ProductQuantity;
    private double Price;

    public UserCart(){}

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    private double TotalPrice;

    public UserCart(String productTitle, String imagePath, String id, int productQuantity, double price, double totalPrice){
    }

    public UserCart(String productTitle, String imagePath, int ID, int productQuantity, double price, double totalPrice) {
        ProductTitle = productTitle;
        ImagePath = imagePath;
        this.ID = ID;
        ProductQuantity = productQuantity;
        Price = price;
        TotalPrice = totalPrice;
    }
}

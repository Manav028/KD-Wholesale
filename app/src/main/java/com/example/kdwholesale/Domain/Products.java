package com.example.kdwholesale.Domain;

import java.io.Serializable;

public class Products implements Serializable{
    private boolean BestProduct;
    private int CategoryID;
    private String Description;
    private int ID;
    private double Price;
    private String Title;
    private String ImagePath;
    private int Quantity;

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public Products(){
    }

    public boolean isBestProduct() {
        return BestProduct;
    }

    public void setBestProduct(boolean bestProduct) {
        BestProduct = bestProduct;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

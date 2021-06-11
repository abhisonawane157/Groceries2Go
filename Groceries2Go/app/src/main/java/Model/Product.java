package Model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Product {

    private String pId;
    private String pName;
    private String details;
    private String category;
    private String photo;
    private double price;

    public Product(String pId, String pName, String details, String category, String photo, double price) {
        this.pId = pId;
        this.pName = pName;
        this.details = details;
        this.category = category;
        this.photo = photo;
        this.price = price;
    }
    public Product(){}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return pName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}



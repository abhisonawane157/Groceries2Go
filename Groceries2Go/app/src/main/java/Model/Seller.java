package Model;

import androidx.annotation.NonNull;

public class Seller {
    private String id;
    private String sellerName;
    private String email;
    private String mobile;
    private String password;
    private String address;
    private String state;
    private String city;
    private String country;
    private String photo;
    private int deliveryFee;
    private String shopName;

    public Seller(String id, String sellerName, String email, String mobile, String password, String address, String state, String city, String country, String photo, int deliveryFee, String shopName) {
        this.id = id;
        this.sellerName = sellerName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.address = address;
        this.state = state;
        this.city = city;
        this.country = country;
        this.photo = photo;
        this.deliveryFee = deliveryFee;
        this.shopName = shopName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

package com.example.kdwholesale.Domain;

import java.io.Serializable;

public class Address implements Serializable {
    private String street;
    private String city;
    private String postcode;

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public Address(){
    }
    public Address(String street, String city, String postcode) {
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }
}

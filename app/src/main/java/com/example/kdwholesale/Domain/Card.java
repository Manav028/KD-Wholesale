package com.example.kdwholesale.Domain;

import java.io.Serializable;

public class Card implements Serializable {
    String cardholdername;
    String cardnumber;
    String cardexpirydate;
    String cardCVV;

    public Card(String cardholdername, String cardnumber, String cardexpirydate, String cardCVV) {
        this.cardholdername = cardholdername;
        this.cardnumber = cardnumber;
        this.cardexpirydate = cardexpirydate;
        this.cardCVV = cardCVV;
    }

    public Card(){}

    public String getCardholdername() {
        return cardholdername;
    }

    public void setCardholdername(String cardholdername) {
        this.cardholdername = cardholdername;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getCardexpirydate() {
        return cardexpirydate;
    }

    public void setCardexpirydate(String cardexpirydate) {
        this.cardexpirydate = cardexpirydate;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }
}


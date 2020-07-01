package com.app.audiobook.audio.service.book;

public class BookPrice {

    public static final String TYPE_FREE = "TYPE_FREE";
    public static final String TYPE_USUAL_PRICE = "TYPE_USUAL_PRICE";
    public static final String TYPE_DISCOUNT_PRICE = "TYPE_DISCOUNT_PRICE";

    private String type;
    private String price;
    private float discount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}

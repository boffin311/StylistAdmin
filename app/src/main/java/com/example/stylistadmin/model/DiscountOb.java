package com.example.stylistadmin.model;

public class DiscountOb {
    int discount;
    int zalonindiscount;

    public int getDiscount() {
        return discount;
    }

    public int getZalonindiscount() {
        return zalonindiscount;
    }

    public DiscountOb(int discount, int zalonindiscount) {
        this.discount = discount;
        this.zalonindiscount = zalonindiscount;
    }
}

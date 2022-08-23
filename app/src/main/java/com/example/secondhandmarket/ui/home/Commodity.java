package com.example.secondhandmarket.ui.home;

public class Commodity {
    String commodityName;
    String commodityPrice;
    int commodityImageId;
    public Commodity(String Name, String Price, int ImageId){
        this.commodityName = Name;
        this.commodityPrice = Price;
        this.commodityImageId = ImageId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public int getCommodityImageId() {
        return commodityImageId;
    }

    public void setCommodityImageId(int commodityImageId) {
        this.commodityImageId = commodityImageId;
    }
}

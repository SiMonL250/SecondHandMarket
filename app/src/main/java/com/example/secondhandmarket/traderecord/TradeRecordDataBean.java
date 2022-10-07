package com.example.secondhandmarket.traderecord;

import java.util.List;

public class TradeRecordDataBean {
    private String buyerAvatar;
    private long buyerId;
    private String buyerName;
    private long createTime;
    private String goodsDescription;
    private long goodsId;
    private long id;
    private List<String> imageUrlList;
    private int price;
    private String sellerAvatar;
    private long sellerId;
    private String sellerName;

    public String getBuyerAvatar() {
        return buyerAvatar;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public long getId() {
        return id;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public int getPrice() {
        return price;
    }

    public String getSellerAvatar() {
        return sellerAvatar;
    }

    public long getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }
}
/*
    "records": [
      {
        "buyerAvatar": "string",
        "buyerId": 0,
        "buyerName": "string",
        "createTime": 0,
        "goodsDescription": "string",
        "goodsId": 0,
        "id": 0,
        "imageUrlList": [
          {}
        ],
        "price": 0,
        "sellerAvatar": "string",
        "sellerId": 0,
        "sellerName": "string"
      }
 */
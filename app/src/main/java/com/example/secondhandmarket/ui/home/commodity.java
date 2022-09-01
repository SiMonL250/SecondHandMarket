package com.example.secondhandmarket.ui.home;

import java.util.Arrays;

public class commodity {//内部类data里的records数组
    private String addr;
    private String appKey;
    private String avatar;
    private String content;
    private int createTime;
    private int id;
    private int imageCode;
    private Arrays imageUrlList;
    private int price;
    private int status;
    private int tUserId;
    private int tuserId;
    private int typeId;
    private String typeName;
    private String username;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageCode() {
        return imageCode;
    }

    public void setImageCode(int imageCode) {
        this.imageCode = imageCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Arrays getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(Arrays imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTuserId() {
        return tuserId;
    }

    public void setTuserId(int tuserId) {
        this.tuserId = tuserId;
    }

    public int gettUserId() {
        return tUserId;
    }

    public void settUserId(int tUserId) {
        this.tUserId = tUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
/*
{
  "data": {
    "current": 0,
    "records": [
      {
        "addr": "string",
        "appKey": "string",
        "avatar": "string",
        "content": "string",
        "createTime": 0,
        "id": 0,
        "imageCode": 0,
        "imageUrlList": [
          {}
        ],
        "price": 0,
        "status": 0,
        "tUserId": 0,
        "tuserId": 0,
        "typeId": 0,
        "typeName": "string",
        "username": "string"
      }
    ],
    "size": 0,
    "total": 0
  }
}
 */
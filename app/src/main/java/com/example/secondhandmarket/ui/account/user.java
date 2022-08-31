package com.example.secondhandmarket.ui.account;

import java.io.Serializable;

public class user implements Serializable {
    private  String userId;
    private  String userNmae;
    private String phone;
    private String avatar;
    private String addr;



    private int money = 100;

    public user(String un, String ph) {
        this.userNmae = un;
        this.phone = ph;
    }

    public user() {

    }

    public String getUserid() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }


    public String getAvatar() {
        return avatar;
    }


    public String getAddr() {
        return addr;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}

package com.example.secondhandmarket.ui.account;

import java.io.Serializable;

public class user implements Serializable {
    private final String userId;
    private final String userNmae;
    private String phone;
    private String avatar;
    private String addr;

    public user(String id,String un, String ph) {
        this.userId = id;
        this.userNmae = un;
        this.phone = ph;
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

}

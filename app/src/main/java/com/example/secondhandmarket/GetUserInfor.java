package com.example.secondhandmarket;

import android.content.Context;
import android.content.SharedPreferences;

public class GetUserInfor {
    public String MYSP_USER = "user";
    public String MYSP_USERNAME = "userName";
    public String MYSP_AVATAR = "avatar";
    public String MYSP_USERID = "userId";
    public String MYSP_MONEY = "money";
    public String MYSP_ISLOGIN = "islogin";
    private Context context;
    private SharedPreferences sp;


    public GetUserInfor(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(MYSP_USER,Context.MODE_PRIVATE);
    }

    public GetUserInfor() {
    }
    public String getUSerName(){
        return sp.getString(MYSP_USERNAME,context.getString(R.string.username));
    }
    public String getAvatar(){
        return sp.getString(MYSP_AVATAR,null);
    }
    public long getUSerID(){
        return sp.getLong(MYSP_USERID,-1);
    }
    public int getUserMoney(){
        return sp.getInt(MYSP_MONEY, 0);
    }
    public boolean getIsLogin(){
        return sp.getBoolean(MYSP_ISLOGIN,false);
    }
}

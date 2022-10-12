package com.example.secondhandmarket.commoditybean;

public class AllGoodsBean {//可以用来获取商品信息
    private String msg;
    private int code;
    private AllGoodsListBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AllGoodsListBean getData() {
        return data;
    }

    public void setData(AllGoodsListBean data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "{" +
                "\"msg\":" +"\""+ msg + '\"' +
                ", \"code\":" + code +
                ", \"data\":" + data +
                '}';
    }
}

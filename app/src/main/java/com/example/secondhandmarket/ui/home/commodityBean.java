package com.example.secondhandmarket.ui.home;

public class commodityBean {

    private String msg;
    private int code;
    private data data;

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

    public commodityBean.data getData() {
        return data;
    }

    public void setData(commodityBean.data data) {
        this.data = data;
    }

    private class data {
        private int current = 0;
        private commodity[] records;
        private int size;
        private int total;
    }
}
/*
{
  "msg": "string",
  "code": 0,
  "data": {//data object
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
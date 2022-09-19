package com.example.secondhandmarket.uploadPicture;

public class UpLoadResultBean {
    private String msg;
    private int code;
    private UpLoadResultBeanData data;

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

    public UpLoadResultBeanData getData() {
        return data;
    }

    public void setData(UpLoadResultBeanData data) {
        this.data = data;
    }
}
/*
{
  "msg": "string",
  "code": 0,
  "data": {
    "imageCode": 0,
    "imageUrlList": [
      {}
    ]
  }
}
 */
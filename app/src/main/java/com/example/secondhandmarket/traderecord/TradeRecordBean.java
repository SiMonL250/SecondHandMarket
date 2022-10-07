package com.example.secondhandmarket.traderecord;

import java.util.List;

public class TradeRecordBean {
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

    public TradeRecordBean.data getData() {
        return data;
    }

    public void setData(TradeRecordBean.data data) {
        this.data = data;
    }

    static class data{
        int current;
        List<TradeRecordDataBean> records;//用到的是这个list
        int size;
        int total;

        public int getCurrent() {
            return current;
        }

        public List<TradeRecordDataBean> getRecords() {
            return records;
        }

        public int getSize() {
            return size;
        }

        public int getTotal() {
            return total;
        }
    }
}
/*
{
  "msg": "string",
  "code": 0,
  "data": {
    "current": 0,
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
    ],
    "size": 0,
    "total": 0
  }
}
 */
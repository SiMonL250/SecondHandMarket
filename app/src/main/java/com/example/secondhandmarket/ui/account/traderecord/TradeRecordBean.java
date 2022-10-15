package com.example.secondhandmarket.ui.account.traderecord;

import com.example.secondhandmarket.BaseResponseBody;

import java.util.List;

public class TradeRecordBean extends BaseResponseBody {

    private data data;

    public TradeRecordBean.data getData() {
        return data;
    }

    static class data{
        int current;
        List<TradeRecordData> records;//用到的是这个list
        int size;
        int total;

        public int getCurrent() {
            return current;
        }

        public List<TradeRecordData> getRecords() {
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
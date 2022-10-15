package com.example.secondhandmarket.ui.home.commodityResponseBody;

public class GoodSingleInfor extends GotCommodityBean{
    //继承，直接用
    private  int appIsShare;

    public int getAppIsShare() {
        return appIsShare;
    }

    public void setAppIsShare(int appIsShare) {
        this.appIsShare = appIsShare;
    }
}
/*
{
  "code": 200,
  "msg": "成功",
  "data": {
    "id": "18",
    "appKey": "bf8da9cfafbb4bd29c8b0b1570e1a117",
    "tUserId": "13",
    "imageCode": "1566069915104972800",
    "content": "sgg",
    "price": 5454,
    "addr": "各睡各的",
    "typeId": 1,
    "typeName": "手机",
    "status": 1,
    "createTime": "1662269399774",
    "username": "15123902554",
    "avatar": "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/04/a03eb2f0-f01b-4f74-8314-2aa7e4e8dbff.jpg",
    "imageUrlList": [
      "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/03/2c285ead-50db-42a3-96a4-41e28e7d2726.jpg",
      "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/03/7f7c357e-c196-4aa7-b7a8-b39a2af669e6.jpg"
    ],
    "appIsShare": 1,
    "tuserId": "13"
  }
}
 */
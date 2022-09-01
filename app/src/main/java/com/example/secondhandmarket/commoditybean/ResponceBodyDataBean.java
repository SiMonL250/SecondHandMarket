package com.example.secondhandmarket.commoditybean;

import java.util.List;

public class ResponceBodyDataBean {
    private int current;
    private List<GotCommodityBean> records;
    private int size;
    private int total;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<GotCommodityBean> getRecords() {
        return records;
    }

    public void setRecords(List<GotCommodityBean> records) {
        this.records = records;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
/*
data:{
current:0
records:[
{
addr:"string"
appKey:"string"
avatar:"string"
content:"string"
createTime:0
id:0
imageCode:0
imageUrlList:[
{}
]
price:0
status:0
tUserId:0
tuserId:0
typeId:0
typeName:"string"
username:"string"
}
]
size:0
total:0
}
 */
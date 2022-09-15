package com.example.secondhandmarket.commoditybean;

import java.util.List;

public class GotCommodityBean {
//    {
//        addr:"string"
//        appKey:"string"
//        avatar:"string"
//        content:"string"
//        createTime:0
//        id:0
//        imageCode:0
//        imageUrlList:[
//        {}
//]
//        price:0
//        status:0
//        tUserId:0
//        tuserId:0
//        typeId:0
//        typeName:"string"
//        username:"string"
//    }
    protected String addr;
    protected String appKey;
    protected String avatar;
    protected String content;
    protected long createTime;
    protected long id;
    protected long imageCode;
    protected List<String> imageUrlList;
    protected int price;
    protected int status;
    protected long tUserId;
    protected long tuserId;
    protected int typeId;
    protected String typeName;
    protected String username;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getImageCode() {
        return imageCode;
    }

    public void setImageCode(int imageCode) {
        this.imageCode = imageCode;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long gettUserId() {
        return tUserId;
    }

    public void settUserId(int tUserId) {
        this.tUserId = tUserId;
    }

    public long getTuserId() {
        return tuserId;
    }

    public void setTuserId(int tuserId) {
        this.tuserId = tuserId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String img(List<String> imageUrlList){
        String s = "";
        for(int i=0; i<imageUrlList.size(); i++){
            s = s +"\""+ imageUrlList.get(i)+"\""+",";
        }
        return "["+s.substring(0,s.length()-1)+"]";
    }
    @Override
    public String toString() {
        return "{" +
                "\"addr\":" +      "\""+addr +  '\"' +
                ", \"appKey\":" +  "\""+appKey + '\"' +
                ", \"avatar\":" +  "\""+avatar + '\"' +
                ", \"content\":" + "\""+content + '\"' +
                ", \"createTime\":" +createTime +
                ", \"id\":" + id +
                ", \"imageCode\":" +imageCode +
                ", \"imageUrlList\":" + img(this.imageUrlList) +
                ", \"price\":" +  price +
                ", \"status\":" + status +
                ", \"tUserId\":" + tUserId +
                ", \"tuserId\":" +tuserId +
                ", \"typeId\":" +typeId +
                ", \"typeName\":" +"\""+ typeName + '\"' +
                ", \"username\":" + "\""+username + '\"' +
                '}';
    }
}

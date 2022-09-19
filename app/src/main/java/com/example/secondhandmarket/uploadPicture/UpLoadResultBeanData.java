package com.example.secondhandmarket.uploadPicture;

import java.util.List;

public class UpLoadResultBeanData {
    private long imageCode; //imageCode
    private List<String> imageUrlList;

    public long getImageCode() {
        return imageCode;
    }

    public void setImageCode(long imageCode) {
        this.imageCode = imageCode;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}

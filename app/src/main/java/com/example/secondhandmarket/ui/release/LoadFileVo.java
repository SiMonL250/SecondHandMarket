package com.example.secondhandmarket.ui.release;

import android.graphics.Bitmap;

import java.io.File;

public class LoadFileVo {

    File file;


    boolean isUpload = false; //标识该文件是否上传

    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public LoadFileVo() {
    }

    public LoadFileVo(File file) {
        this.file = file;
    }

    public LoadFileVo(File file, boolean isUpload,Bitmap bitmap) {
        this.file = file;

        this.isUpload = isUpload;
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
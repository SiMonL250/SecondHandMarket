package com.example.secondhandmarket.getURLimage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class getURLimage {
    private Context context;

    public getURLimage(Context context) {
        this.context = context;
    }


    public Bitmap getimage(String url) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.INTERNET}, 1);
        }

        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();

            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public Drawable getDrawableImage(String url){
        Drawable drawable = null;
        try {
            URL myurl = new URL(url);
            URLConnection conn = myurl.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            drawable = Drawable.createFromStream(in, "background.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }
}

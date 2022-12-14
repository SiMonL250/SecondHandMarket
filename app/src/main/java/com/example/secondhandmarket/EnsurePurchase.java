package com.example.secondhandmarket;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.secondhandmarket.appSecret.AppSecret;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EnsurePurchase extends AppCompatActivity {
    private Map<String,Object> map;
    private ImageView ivEnsurePic;
    private TextView tvEnsureName, tvEnsurePrice;
    private Button btnEnsureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_purchase);

        Bundle extras = getIntent().getExtras();

        PurchaseMap purchaseMap = (PurchaseMap) extras.getSerializable("purchaseParams");
        map = purchaseMap.getBuyingparams();
        System.out.println(map);

        ivEnsurePic = findViewById(R.id.ensure_buy_pic);
        tvEnsureName = findViewById(R.id.ensure_buy_name);
        tvEnsurePrice = findViewById(R.id.ensure_buy_price);
        btnEnsureBtn = findViewById(R.id.ensure_buy_btn);

        if(map!=null){

            String ensureName = (String) map.get("goodName");
            long sellerId = (long) map.get("sellerId");
            long buyerId = (long) map.get("buyerId");
            long goodsId = (long) map.get("goodId");
            int goodPrice = (int) map.get("goodsPrice");
            //good name
            if (ensureName != null) {
                tvEnsureName.setText(ensureName);
                 if (ensureName.length() > 10) {

                        tvEnsureName.setFocusable(true);
                        tvEnsureName.setFocusableInTouchMode(true);
                        tvEnsureName.setSelected(true);
                }
            }
            //good price
            tvEnsurePrice.setText(Integer.toString(goodPrice));
            //good picture
            String url = (String) map.get("picture");
            if(url!=null){

                new Thread(()->{
                    getURLimage getimage = new getURLimage(this);
//                    Bitmap bm = new getURLimage(this).getimage(url);
                    Drawable drawable = getimage.getDrawableImage(url);
                    if(drawable!=null){
                        Message msg = new Message();
                        msg.what = 0x1222;
                        msg.obj = drawable;

                        new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0x1222){
//                                    ivEnsurePic.setImageBitmap((Bitmap) msg.obj);
                                    ivEnsurePic.setImageDrawable((Drawable) msg.obj);
                                }
                            }
                        }.sendMessage(msg);
                    }
                }).start();
            }
            //ensure btn
            btnEnsureBtn.setOnClickListener(v -> {
                //?????????map??????  ?????? map ??????null
                Log.d("test","TTTTTTT");

                purchase(buyerId,goodsId,goodPrice,sellerId);
            });
        }
    }

    private void purchase(long buyerID, long goodID, int price, long sellerID) {
        new Thread(() -> {
            // url??????
//            String url = "http://47.107.52.7:88/member/tran/trading?buyerId=" + buyerID
//                    + "&goodsId=" + goodID +
//                    "&price=" + price + "&sellerId=" + sellerID;
            String url = "http://47.107.52.7:88/member/tran/trading";
//            System.out.println(url);
            // ?????????
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new AppSecret().appID)
                    .add("appSecret", new AppSecret().appSecret)
                    .add("Content-Type", "application/json")
                    .build();

            Map<String,Object> map = new HashMap<>();
            map.put("buyerId",buyerID);
            map.put("goodsId",goodID);
            map.put("price",price);
            map.put("sellerId",sellerID);

            String body = new Gson().toJson(map);
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //??????????????????
            Request request = new Request.Builder()
                    .url(url)
                    // ???????????????????????????
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //?????????????????????callback????????????
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(EnsurePurchase.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body!=null;
                        purchaseResult<Object> result = new purchaseResult<>();
                        result = new Gson().fromJson(new String(body.bytes()),result.getClass());

                        System.out.println(result.toString());
                        if(result.getCode() == 200){
                            //????????????
                            startActivity(new Intent(EnsurePurchase.this, MainActivity.class));
                            finish();
                        }else{
                            Looper.prepare();
                            Toast.makeText(EnsurePurchase.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }
        ).start();
    }

/*
??????
{
msg:"string"
code:0
data:{}
}
 */
    /**
     * http????????????????????????
     * @param <T> ??????
     */
    public static class purchaseResult<T> {

        /**
         * ???????????????
         */
        private int code;
        /**
         * ??????????????????
         */
        private String msg;
        /**
         * ????????????
         */
        private T data;

        public purchaseResult(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}
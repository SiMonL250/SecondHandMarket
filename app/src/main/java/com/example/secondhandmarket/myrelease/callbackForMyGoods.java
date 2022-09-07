package com.example.secondhandmarket.myrelease;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class callbackForMyGoods {
    private final List<Map<String, String>> L = new ArrayList<>();

    public Callback callback(int status, Context context){
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.prepare();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                ResponseBodyBean responseBodyBean = new ResponseBodyBean();
                try {
                    assert body != null;
                    responseBodyBean = new Gson().fromJson(new String(body.bytes()), responseBodyBean.getClass());
                }catch (IOException e){
                    e.printStackTrace();
                }
                Toast.makeText(context, responseBodyBean.getMsg(), Toast.LENGTH_SHORT).show();
                if(responseBodyBean.getCode() == 200){
                    List<GotCommodityBean> listtemp = responseBodyBean.getData().getRecords();
                    //遍历listtemp 存数据
                    for (int i = 0; i < listtemp.size(); i++) {
                        GotCommodityBean m = listtemp.get(i);
                        if(m.getStatus() == status){//检测状态，是否卖出
                            //TODO set Map here
                            //TODO set L
                            Log.d("test", "onResponse: ");
                        }
                    }
                }

            }

        };
    }

    public List<Map<String, String>> getL() {
        return L;
    }
}

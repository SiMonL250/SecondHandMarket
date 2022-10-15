package com.example.secondhandmarket;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;

public class others {
    private int i = 60;
    public void changeBtnGetCode( Button getCodeButton, Activity activity) {

        new Thread(()->{
            boolean tag = true;

            if (tag) {
                while (i > 0) {
                    i--;
                    //如果活动为空
                    activity.runOnUiThread(() -> {
                        getCodeButton.setText( i + "s");
                        getCodeButton.setClickable(false);
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tag = false;
            }
            i = 60;
            tag = true;
            activity.runOnUiThread(() -> {
                getCodeButton.setText("输入验证码");
                // getCodeButton.setClickable(false);
            });
        }).start();
    }

    public boolean isMobileNO(String sphone) {
        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(sphone))
            return false;
        else
            return sphone.matches(telRegex);

    }
}

package com.example.secondhandmarket.ui.notifications;

import java.util.List;

public class myNotifi {
    private String msg;
    private int code;
    private List<myNotifi.data> data;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public List<myNotifi.data> getData() {
        return data;
    }

    static class data {
        private long fromUserId;
        private int unReadNum;
        private String username;

        public long getFromUserId() {
            return fromUserId;
        }

        public int getUnReadNum() {
            return unReadNum;
        }

        public String getUsername() {
            return username;
        }
    }
}

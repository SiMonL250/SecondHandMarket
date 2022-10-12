package com.example.secondhandmarket;

import java.io.Serializable;
import java.util.Map;

public class PurchaseMap implements Serializable {
    Map<String,Object> Buyingparams;

    public PurchaseMap(Map<String, Object> buyingparams) {
        Buyingparams = buyingparams;
    }

    public Map<String, Object> getBuyingparams() {
        return Buyingparams;
    }
}

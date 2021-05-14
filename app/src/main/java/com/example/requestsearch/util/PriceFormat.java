package com.example.requestsearch.util;

/**
 * 가격 변환 클래스
 */
public class PriceFormat {
    public String getPriceFormat(String price){
        if(price.contains(".")) {
            int index=price.indexOf(".");
            price=price.substring(0,index);
        }
        return String.format("%,d", Integer.parseInt(price));
    }
}

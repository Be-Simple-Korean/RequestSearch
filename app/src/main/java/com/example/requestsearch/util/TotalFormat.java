package com.example.requestsearch.util;

public class TotalFormat {
    public String getTotalFormat(String type,int total){
        String result="";
        if(total==0){
            return type;
        }else{
            if(total>9999){
                return type+"(9999)";
            }else{
                return type+"("+total+")";
            }
        }
    }
}

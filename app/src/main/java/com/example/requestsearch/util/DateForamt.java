package com.example.requestsearch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 날짜 형식 변환 클래스
 */
public class DateForamt {
    /**
     * 날짜 형식 필터링메소드
     * @param date 네이버에서 가져오는 날짜데이터
     * @return 필터링된 날짜데이터
     */
    public String getDateFormat(String date){
        String newPubDate="";
        SimpleDateFormat oldDataFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            java.util.Date formatDate = oldDataFormat.parse(date);
            newPubDate = newDateFormat.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newPubDate;
    }
}

package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.dialog.SelectOptionDialog;
import com.example.requestsearch.dialog.GenreDialog;

/**
 * 다이얼로그 닫기 리스너
 */
public interface OnDimissListener {
    void onDismissed(GenreDialog dialog, boolean isItem,int position);
    void onDismissed(SelectOptionDialog dialog, int position, String sorte,String range);

}

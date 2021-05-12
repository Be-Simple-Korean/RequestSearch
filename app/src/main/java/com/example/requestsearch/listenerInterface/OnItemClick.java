package com.example.requestsearch.listenerInterface;

import android.view.View;

/**
 * 아이템 클릭 리스너
 */
public interface OnItemClick {
    void onItemClick(View v, int position,String word);
}

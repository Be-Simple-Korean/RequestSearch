package com.example.requestsearch.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.telecom.Call;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.adapter.DataAdapter;

/**
 * Recyclerview 아이템 여백 설정
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int top;
    public ItemDecoration(Context context) {
        top = dpTopx(context, 10);
    }

    private int dpTopx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = top;
        DataAdapter dataAdapter =new DataAdapter(null);
        if(dataAdapter.isFinished()){
            Log.e("수행","!!!");
            outRect.bottom=50;
        }
    }


}

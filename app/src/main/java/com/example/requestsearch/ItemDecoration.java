package com.example.requestsearch;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        int position = parent.getChildAdapterPosition(view);
        int count = state.getItemCount();
        outRect.top = top;
    }
}

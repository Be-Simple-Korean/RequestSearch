package com.example.requestsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreItemDecoration extends RecyclerView.ItemDecoration {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;

    private Drawable mDivider;
    private int spanCount;
    private int lastIndex;
    public GenreItemDecoration(Context context, int spanCount) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        this.spanCount = spanCount;
        lastIndex=spanCount-1;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            if (i == FIRST || i == SECOND || i == THIRD) {
                //위쪽구분선
                int left = child.getLeft() - child.getPaddingLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            //왼쪽구분선
            if (i % spanCount == 0) {
                int left = child.getLeft() - child.getPaddingLeft();
                int right = left + mDivider.getIntrinsicWidth();
                int top = child.getTop();
                int bottom = child.getBottom();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

            //아래쪽구분선
            int left = child.getLeft() - child.getPaddingLeft();
            int right = child.getRight();
            int top = child.getBottom();
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            int verticalTop = child.getTop();
            int verticalBottom = child.getBottom();
            int verticalLeft = child.getRight() - mDivider.getIntrinsicWidth();
            int verticalRight = verticalLeft + mDivider.getIntrinsicWidth();
            mDivider.setBounds(verticalLeft, verticalTop, verticalRight, verticalBottom);
            Log.e("data", verticalLeft + "/" + verticalTop + "/" + verticalRight + "/" + verticalBottom);
            mDivider.draw(c);
        }

    }
    
}

package com.example.requestsearch.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.adapter.GridAdapter;
import com.example.requestsearch.data.movie.MovieGenreDataVO;

import java.util.ArrayList;

public class GenreItemDecoration extends RecyclerView.ItemDecoration {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;

    private Drawable mDivider;
    private int left;
    private int right;
    private int bottom;
    private int top;
    private Context context;

    public GenreItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            drawRect(i, c, child,false);
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        mDivider = ContextCompat.getDrawable(context, R.drawable.select_divider);
        for (int i = 0; i < childCount; i++) { // 0 = selected
            View childView = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(childView);
            if (viewHolder instanceof GridAdapter.GenreViewHolder) {
                GridAdapter.GenreViewHolder genreHolder = (GridAdapter.GenreViewHolder) viewHolder;

                if (genreHolder.isSelected()) {
                    drawRect(i, c, childView,true);
                }
            }

        }

    }

    /**
     * 아이템 테두리 draw
     *
     * @param i
     * @param c
     * @param child
     */
    private void drawRect(int i, Canvas c, View child,boolean isSelect) {

        //위쪽구분선
        left = child.getLeft() - child.getPaddingLeft();
        right = child.getRight();
        top = child.getTop();
        if(i==FIRST||i==SECOND||i==THIRD||isSelect){
            bottom = top + mDivider.getIntrinsicHeight();
        }else{
            bottom = top - mDivider.getIntrinsicHeight();
        }
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);

        //아래쪽구분선
        left = child.getLeft() - child.getPaddingLeft();
        right = child.getRight();
        top = child.getBottom();
        bottom = top + mDivider.getIntrinsicHeight();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);

        //왼쪽구분선
        left = child.getLeft() - child.getPaddingLeft();
        right = left + mDivider.getIntrinsicWidth();
        top = child.getTop();
        bottom = child.getBottom();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);

        //오른쪽
        int verticalTop = child.getTop();
        int verticalBottom = child.getBottom();
        int verticalLeft = child.getRight();
        int verticalRight = verticalLeft + mDivider.getIntrinsicWidth();
        mDivider.setBounds(verticalLeft, verticalTop, verticalRight, verticalBottom);
        mDivider.draw(c);
    }

}

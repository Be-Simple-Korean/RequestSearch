package com.example.requestsearch.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;

import java.util.ArrayList;

public class OptionItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int left;
    private int right;
    private int bottom;
    private int top;
    private ArrayList<Boolean> isSelected;
    private Context context;

    public OptionItemDecoration(Context context, ArrayList<Boolean> isSelected) {
        this.context = context;
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        this.isSelected = isSelected;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            drawRect(i,c,child);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isSelected.get(i) != null) {
                if (isSelected.get(i)) {
                    mDivider = ContextCompat.getDrawable(context, R.drawable.select_divider);
                    View child = parent.getChildAt(i);
                    drawRect(i,c,child);
                }
            }
        }
    }

    private void drawRect(int i, Canvas c, View child){
        //위쪽
        left = child.getLeft() - child.getPaddingLeft();
        right = child.getRight();
        top = child.getTop();
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

        //아래쪽구분선
        left = child.getLeft() - child.getPaddingLeft();
        right = child.getRight();
        top = child.getBottom() - mDivider.getIntrinsicHeight();
        bottom = top + mDivider.getIntrinsicHeight();
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

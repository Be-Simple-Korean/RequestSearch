package com.example.requestsearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.adapter.decoration.OptionItemDecoration;
import com.example.requestsearch.R;
import com.example.requestsearch.adapter.GridAdapter;
import com.example.requestsearch.listenerInterface.OnDimissListener;
import com.example.requestsearch.listenerInterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 옵션 항목 표시 ui수정 다이얼로그
 */
public class SelectOptionDialog extends Dialog {

    private Context context;
    private String sort;
    private String d_range;
    private String type;

    private OnDimissListener onDimissListener = null;
    private GridAdapter gridAdapter;

    public SelectOptionDialog(@NonNull Context context, String sort, String d_range, String type) {
        super(context);
        this.sort = sort;
        this.d_range = d_range;
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option_select);
        gridAdapter = new GridAdapter();
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);
        }

        int sortIndex = 0; // 기본 - sim
        if (sort.equals("date")) {
            sortIndex = 1;
        } else if (sort.equals("count")) {
            sortIndex = 2;
        }
        setRecyclerViewSort(sortIndex);

        int rangeIndex = 0; // 기본 - 전체
        if (d_range.equals("책제목")) {
            rangeIndex = 1;
        } else if (d_range.equals("저자")) {
            rangeIndex = 2;
        } else if (d_range.equals("출판사")) {
            rangeIndex = 3;
        }
        setRecyclerViewRange(rangeIndex);

        // X(닫기) 이미지 버튼형식
        ImageView ivCloseDialog = findViewById(R.id.imageview_dialog_close);
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 정렬 세팅
     *
     * @param sortIndex
     */
    private void setRecyclerViewSort(int sortIndex) {
        String[] sortArray = {"관련도순", "출간일순", "판매량순"};
        ArrayList<String> sortTitle = new ArrayList<String>(Arrays.asList(sortArray));
        ArrayList<Boolean> isSelected = new ArrayList<>();
        for (int i = 0; i < sortTitle.size(); i++) {
            if (sortIndex == i) {
                isSelected.add(true);
            } else {
                isSelected.add(false);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerview_option_sort);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), sortTitle.size());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new OptionItemDecoration(getContext(), isSelected));

        gridAdapter.setSortList(sortTitle);
        gridAdapter.setIsSelected(isSelected);
        gridAdapter.setType(type);
        gridAdapter.notifyDataSetChanged();

        gridAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void setOnItemClick(View v, int position) {
                d_range="전체";
                switch (position) {
                    case 0:
                        sort = "sim";
                        break;
                    case 1:
                        sort = "date";
                        break;
                    case 2:
                        sort = "count";
                        break;
                }
                onDimissListener.onDismissed(SelectOptionDialog.this, position, sort,d_range);
            }
        });
        recyclerView.setAdapter(gridAdapter);
    }


    /**
     * 정렬 세팅
     *
     * @param rangeIndex
     */
    private void setRecyclerViewRange(int rangeIndex) {
        String[] rangeArray = {"전체", "책제목", "저자", "출판사"};
        ArrayList<String> rangeTitle = new ArrayList<String>(Arrays.asList(rangeArray));
        ArrayList<Boolean> isSelected = new ArrayList<>();

        for (int i = 0; i < rangeTitle.size(); i++) {
            if (rangeIndex == i) {
                isSelected.add(true);
            } else {
                isSelected.add(false);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerview_option_range);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), rangeTitle.size());

        recyclerView.setLayoutManager(gridLayoutManager);
        GridAdapter gridRandgeAdapter = new GridAdapter();
        gridRandgeAdapter.setSortList(rangeTitle);
        gridRandgeAdapter.setIsSelected(isSelected);
        gridRandgeAdapter.setType(type);
        gridRandgeAdapter.notifyDataSetChanged();
        recyclerView.addItemDecoration(new OptionItemDecoration(getContext(), isSelected));

        gridRandgeAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void setOnItemClick(View v, int position) {
                switch (position) {
                    case 0:
                        d_range = "전체";
                        break;
                    case 1:
                        d_range = "책제목";
                        break;
                    case 2:
                        d_range = "저자";
                        break;
                    case 3:
                        d_range = "출판사";
                        break;
                }
                onDimissListener.onDismissed(SelectOptionDialog.this, position, sort,d_range);
            }
        });
        recyclerView.setAdapter(gridRandgeAdapter);
    }

    /**
     * 다이얼로그 닫기 리스너
     *
     * @param onDimissListener
     */
    public void setOnDimissListener(OnDimissListener onDimissListener) {
        this.onDimissListener = onDimissListener;
    }
}

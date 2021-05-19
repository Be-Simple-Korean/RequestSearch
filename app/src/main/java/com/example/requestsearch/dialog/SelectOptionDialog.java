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

import com.example.requestsearch.decoration.OptionItemDecoration;
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

    private OnDimissListener onDimissListener=null;

    public void setOnDimissListener(OnDimissListener onDimissListener) {
        this.onDimissListener = onDimissListener;
    }

    public SelectOptionDialog(@NonNull Context context, String sort, String d_range) {
        super(context);
        this.sort = sort;
        this.d_range = d_range;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option_select);
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
     * @param sortIndex
     */
    private void setRecyclerViewSort(int sortIndex) {
        String[] sortArray={"관련도순","출간일순","판매량순"};
        ArrayList<String> sortTitle=new ArrayList<String>(Arrays.asList(sortArray));
        ArrayList<Boolean> isSelected=new ArrayList<>();
        for(int i=0;i<sortTitle.size();i++){
            if(sortIndex==i){
                isSelected.add(true);
            }else{
                isSelected.add(false);
            }
        }
        RecyclerView recyclerView= findViewById(R.id.recyclerview_option_sort);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),sortTitle.size());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new OptionItemDecoration(getContext(),sortTitle.size(),isSelected));


        GridAdapter optionSortAdapter=new GridAdapter(this,sortTitle,isSelected);

        optionSortAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void setOnItemClick(View v,int position) {
                onDimissListener.onDismissed(SelectOptionDialog.this,position,false);
            }
        });
        recyclerView.setAdapter(optionSortAdapter);
    }

    /**
     * 정렬 세팅
     * @param rangeIndex
     */
    private void setRecyclerViewRange(int rangeIndex) {
        String[] rangeArray={"전체","책제목","저자","출판사"};
        ArrayList<String> rangeTitle=new ArrayList<String>(Arrays.asList(rangeArray));
        ArrayList<Boolean> isSelected=new ArrayList<>();
        for(int i=0;i<rangeTitle.size();i++){
            if(rangeIndex==i){
                isSelected.add(true);
            }else{
                isSelected.add(false);
            }
        }
        RecyclerView recyclerView= findViewById(R.id.recyclerview_option_range);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),rangeTitle.size());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new OptionItemDecoration(getContext(),rangeTitle.size(),isSelected));


        GridAdapter optionSortAdapter=new GridAdapter(this,rangeTitle,isSelected);

        optionSortAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void setOnItemClick(View v,int position) {

                onDimissListener.onDismissed(SelectOptionDialog.this,position,true);
            }
        });
        recyclerView.setAdapter(optionSortAdapter);
    }


}

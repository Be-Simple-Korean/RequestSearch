package com.example.requestsearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.requestsearch.R;
import com.example.requestsearch.listenerInterface.OnItemClick;

/**
 * 옵션 항목 표시 다이얼로그
 */
public class SelectOptionDialog extends Dialog {

    private Context context;
    private String sort;
    private String d_range;

    private OnItemClick onItemClick = null;


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
        setSortSelected(sortIndex);

        int rangeIndex = 0; // 기본 - 전체
        if (d_range.equals("책제목")) {
            rangeIndex = 1;
        } else if (d_range.equals("저자")) {
            Log.e("범위수행", "1");
            rangeIndex = 2;
        } else if (d_range.equals("출판사")) {
            rangeIndex = 3;
        }
        setRangeSelected(rangeIndex);

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
     * 정렬 셀렉터 초기화
     *
     * @param index 전에 선택한 위치값
     */
    public void setSortSelected(int index) {
        ViewGroup ll = findViewById(R.id.ll_sort);
        int childCount = ll.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = ll.getChildAt(i);

            view.setSelected(i == index);
//            if(i==index){
//                view.setSelected(true);
//                TextView textView =(TextView) view;
//                Drawable drawable = context.getResources().getDrawable(R.drawable.sort_selecticon);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //drawble 크기 설정
//                textView.setCompoundDrawables(drawable,null,null,null);
//            }else{
//                view.setSelected(false);
//                TextView textView =(TextView) view;
//                Drawable drawable = context.getResources().getDrawable(R.drawable.sort_baseicon);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                textView.setCompoundDrawables(drawable,null,null,null);
//            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("셀렉터수행", "0");
                    if (!view.isSelected()) {
                        Log.e("셀렉터수행", "1");
                        if (onItemClick != null) {
                            Log.e("셀렉터수행", "2");
                            dismiss();
                            onItemClick.onItemClick(v, 1, "");
                        }
                    }
                }
            });
        }
    }

    /**
     * 범위 셀렉터 초기화
     *
     * @param rangeIndex 전에 선택한 위치값
     */
    private void setRangeSelected(int rangeIndex) {
        ViewGroup ll = findViewById(R.id.latout_option_range_select);
        int childCount = ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = ll.getChildAt(i);
            view.setSelected(i == rangeIndex);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!view.isSelected()) {
                        if (onItemClick != null) {
                            dismiss();
                            onItemClick.onItemClick(v, 2, "");
                        }
                    }
                }
            });
        }
    }

    /**
     * 아이템 클릭 리스너 설정
     *
     * @param onItemClick
     */
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}

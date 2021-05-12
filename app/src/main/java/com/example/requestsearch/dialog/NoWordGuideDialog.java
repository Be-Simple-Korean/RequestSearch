package com.example.requestsearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.requestsearch.R;

/**
 * 검색어 없을시 안내 팝업
 */
public class NoWordGuideDialog extends Dialog {

    public NoWordGuideDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_word_guide);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 텍스트뷰 - 취소
        findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 텍스트뷰 - 확인
        findViewById(R.id.tv_dialog_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}

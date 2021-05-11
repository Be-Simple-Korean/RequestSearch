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

public class NoWordGuideDialog extends Dialog {

    public NoWordGuideDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_word_guide);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvCancel = findViewById(R.id.tv_dialog_cancel);
        TextView tvCheck = findViewById(R.id.tv_dialog_check);

        tvCancel.setOnClickListener(new View.OnClickListener() { //취소 텍스트 클릭
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvCheck.setOnClickListener(new View.OnClickListener() { //확인 텍스트 클릭
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

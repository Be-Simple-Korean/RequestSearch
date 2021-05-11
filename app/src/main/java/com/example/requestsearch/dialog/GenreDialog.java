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

import com.example.requestsearch.adapter.MovieGenreAdapter;
import com.example.requestsearch.OnDimissListener;
import com.example.requestsearch.OnItemClickListener;
import com.example.requestsearch.R;
import com.example.requestsearch.data.movie.MovieGenreData;

import java.util.ArrayList;

public class GenreDialog extends Dialog {
    ArrayList<MovieGenreData> genreList;
    public GenreDialog(@NonNull Context context,ArrayList<MovieGenreData> genreList) {
        super(context);
        this.genreList=genreList;
    }
    OnDimissListener onDimissListener=null;

    public void setOnDimissListener(OnDimissListener onDimissListener) {
        this.onDimissListener = onDimissListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_movie_genre_select);
        ImageView ivMovieDialogClose= findViewById(R.id.imageview_movie_dialog_close); // X(닫기) 이미지 버튼형식
        Window window = getWindow();
        if(window!=null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width=WindowManager.LayoutParams.MATCH_PARENT;
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes( params );
            window.setGravity( Gravity.BOTTOM );
        }
        RecyclerView recyclerView=findViewById(R.id.recyclerview_movie_dialog);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        MovieGenreAdapter movieGenreAdapter=new MovieGenreAdapter(this,genreList);
        movieGenreAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void setOnItemClick(View v,int position) {
                onDimissListener.onDismissed(GenreDialog.this,true,position);
            }
        });
        recyclerView.setAdapter(movieGenreAdapter);
        ivMovieDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               onDimissListener.onDismissed(GenreDialog.this,false,0);
            }
        });

    }


}

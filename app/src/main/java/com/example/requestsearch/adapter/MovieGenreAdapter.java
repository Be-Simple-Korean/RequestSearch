package com.example.requestsearch.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.requestsearch.listenerInterface.OnItemClickListener;
import com.example.requestsearch.R;
import com.example.requestsearch.data.movie.MovieGenreDataVO;
import com.example.requestsearch.dialog.GenreDialog;

import java.util.ArrayList;

/**
 * 영화 장르 데이터 Recyclerview Adapter
 */
public class MovieGenreAdapter extends RecyclerView.Adapter<MovieGenreAdapter.GenreViewHolder> {

    private ArrayList<MovieGenreDataVO> genreList;
    private GenreDialog dialog;
    private OnItemClickListener onItemClickListener =null;

    /**
     * 영화 아이템 클릭 리스너
     * @param onItemClickListener
     */
    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MovieGenreAdapter(GenreDialog di, ArrayList<MovieGenreDataVO> genreList) {
        this.dialog=di;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_genre,parent,false);
        GenreViewHolder viewHolder = new GenreViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  GenreViewHolder holder, int position) {
        holder.tvGenre.setText(genreList.get(position).getgName());
        holder.tvGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("bottom",holder.tvGenre.getBottom()+"");
                Log.e("right",holder.tvGenre.getRight()+"");
                Log.e("width",holder.tvGenre.getWidth()+"");
            }
        });
        holder.tvGenre.setSelected(genreList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    /**
     * 영화 장르 항목 뷰홀더
     */
    public class GenreViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvGenre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.textview_movie_dialog_genre);
//
//            tvGenre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    for (int i = 0; i < genreList.size(); i++) {
//                        genreList.get(i).setSelected(false);
//                    }
//                    genreList.get(position).setSelected(true);
//                    notifyDataSetChanged();
//                    if (position != RecyclerView.NO_POSITION) {
//                        if (onItemClickListener != null) {
//                            onItemClickListener.setOnItemClick(v, position + 1);
//                        }
//                    }
//                }
//            });
        }
    }
}

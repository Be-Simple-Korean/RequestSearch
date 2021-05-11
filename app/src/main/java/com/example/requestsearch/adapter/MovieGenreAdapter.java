package com.example.requestsearch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.requestsearch.OnItemClickListener;
import com.example.requestsearch.R;
import com.example.requestsearch.data.movie.MovieGenreData;
import com.example.requestsearch.dialog.GenreDialog;

import java.util.ArrayList;

public class MovieGenreAdapter extends RecyclerView.Adapter<MovieGenreAdapter.GenreViewHolder> {
    ArrayList<MovieGenreData> genreList;
    GenreDialog dialog;
    OnItemClickListener onItemClickListener =null;

    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MovieGenreAdapter(GenreDialog di, ArrayList<MovieGenreData> genreList) {
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
        holder.tvGenre.setSelected(genreList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvGenre;
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre=itemView.findViewById(R.id.textview_movie_dialog_genre);

            tvGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    for(int i=0;i<genreList.size();i++){
                        genreList.get(i).setSelected(false);
                    }
                    genreList.get(position).setSelected(true);
                    notifyDataSetChanged();
                    if(position!= RecyclerView.NO_POSITION){
                        if(onItemClickListener !=null){
                            onItemClickListener.setOnItemClick(v,position+1);
                        }
                    }
                }
            });
        }
    }
}

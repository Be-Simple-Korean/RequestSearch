package com.example.requestsearch.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.network.data.movie.MovieGenreDataVO;
import com.example.requestsearch.listenerInterface.OnItemClickListener;

import java.util.ArrayList;

/**
 * 옵션&장르 데이터 Recyclerview Adapter
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GenreViewHolder> {

    private ArrayList<String> sortList;
    private ArrayList<MovieGenreDataVO> genreList;
    private String type;
    private OnItemClickListener onItemClickListener =null;
    private ArrayList<Boolean> isSelected;


    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        GenreViewHolder viewHolder = new GenreViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  GenreViewHolder holder, int position) {
        if(type.equals("book")){
            holder.tvGenre.setText(sortList.get(position));
            holder.tvGenre.setSelected(isSelected.get(position));
        }else{
            holder.tvGenre.setText(genreList.get(position).getgName());
            holder.tvGenre.setSelected(genreList.get(position).isSelected());
        }


    }

    @Override
    public int getItemCount() {
        if(type.equals("book")){
            return sortList!=null?sortList.size():0;
        }else if (type.equals("movie")){
            return genreList!=null?genreList.size():0;
        }else{
            return 0;
        }
    }

    /**
     * 옵션항목 뷰홀더
     */
    public class GenreViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvGenre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.textview_movie_dialog_genre);

            tvGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(type.equals("book")){
                        isSelected.clear();
                        for (int i = 0; i < sortList.size(); i++) {
                            if (i == position) {
                                isSelected.add(true);
                            } else {
                                isSelected.add(false);
                            }
                        }
                    }else if (type.equals("movie")){
                        for (int i = 0; i < genreList.size(); i++) {
                            if (i == position) {
                                genreList.get(i).setSelected(true);
                            } else {
                                genreList.get(i).setSelected(false);
                            }
                        }
                    }
                    notifyDataSetChanged();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.setOnItemClick(v, position);
                        }
                    }
                }
            });
        }
        public boolean isSelected() {
            return tvGenre != null ? tvGenre.isSelected() : false;
        }
    }

    /**
     * 옵션 아이템 클릭 리스너
     * @param onItemClickListener
     */
    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * 장르 데이터 리스트 설정
     * @param genreList
     */
    public void setGenreList(ArrayList<MovieGenreDataVO> genreList) {
        this.genreList = genreList;
    }

    /**
     * 옵션데이터 리스트 설정
     * @param sortList
     */
    public void setSortList(ArrayList<String> sortList) {
        this.sortList = sortList;
    }

    /**
     * 선택항목값 리스트 설정
     * @param isSelected
     */
    public void setIsSelected(ArrayList<Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 데이터 타입 설정 =  book / movie
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}

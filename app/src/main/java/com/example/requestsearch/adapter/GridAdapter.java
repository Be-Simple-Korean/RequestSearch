package com.example.requestsearch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.dialog.SelectOptionDialog;
import com.example.requestsearch.listenerInterface.OnItemClickListener;

import java.util.ArrayList;

/**
 * 영화 장르 데이터 Recyclerview Adapter
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GenreViewHolder> {

    private ArrayList<String> sortList;
    private SelectOptionDialog dialog;
    private OnItemClickListener onItemClickListener =null;
    ArrayList<Boolean> isSelected;
    /**
     * 영화 아이템 클릭 리스너
     * @param onItemClickListener
     */
    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public GridAdapter(SelectOptionDialog di, ArrayList<String> sortList, ArrayList<Boolean> isSelecte) {
        this.dialog=di;
        this.sortList = sortList;
        this.isSelected=isSelecte;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        GenreViewHolder viewHolder = new GenreViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  GenreViewHolder holder, int position) {
        holder.tvGenre.setText(sortList.get(position));
        holder.tvGenre.setSelected(isSelected.get(position));

    }

    @Override
    public int getItemCount() {
        return sortList.size();
    }

    /**
     * 영화 장르 항목 뷰홀더
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
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.setOnItemClick(v, position);
                        }
                    }
                }
            });
        }
    }
}

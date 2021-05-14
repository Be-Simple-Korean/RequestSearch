package com.example.requestsearch.adapter;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.R;
import com.example.requestsearch.data.movie.MovieItemsVO;

import java.util.ArrayList;

/**
 * 영화 데이터 Recyclerview Adapter
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int NOREUSLT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;
    private static final int MAIN_TYPE = 3;

    private String word;
    private ArrayList<MovieItemsVO> movieMainItemsArrayList;
    private OnItemClick onItemClick=null;
    private RecyclerView recyclerView;
    private View headerView;
    private View noResultview;

    public MovieAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    /**
     * 영화 리사이클러뷰 아이템 클릭 리스너
     * @param onItemClick
     */
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    /**
     * 영화 검색결과 없음을 나타내는 단어
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 영화 검색 데이터리스트 세팅
     * @param movieMainItemsArrayList
     */
    public void setMovieMainItemsArrayList(ArrayList<MovieItemsVO> movieMainItemsArrayList) {
        this.movieMainItemsArrayList = movieMainItemsArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return movieMainItemsArrayList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case HEADER_TYPE:
                headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_movie_header,parent,false);
                return new MovieHeaderHolder(headerView);
            case NOREUSLT_TYPE:
                noResultview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_noresult,parent,false);
                return new NoResultViewHolder(noResultview);
            case LOADMORE_TYPE:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_footer,parent,false);
                return new LoadMoreViewHolder(view);
            default:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_movie,parent,false);
                return new MovieItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType=getItemViewType(position);
            switch (viewType) {
                case NOREUSLT_TYPE:
                    ((NoResultViewHolder) holder).tvFindWord.setText(word);
                    int recyclerViewHeight=recyclerView.getHeight();
                    RecyclerView.LayoutParams header=(RecyclerView.LayoutParams)headerView.getLayoutParams();
                    int headerHeight = header.height;
                    RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)noResultview.getLayoutParams();
                    //dp->px
                    int marginTop=(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 70,
                            holder.itemView.getResources().getDisplayMetrics());
                    params.height=recyclerViewHeight-(headerHeight+marginTop);
                    noResultview.setLayoutParams(params);
                    break;
                case MAIN_TYPE:
                    if (movieMainItemsArrayList.get(position) != null) {
                        showMovieItems((MovieItemViewHolder) holder, position);
                    }
            }
    }

    @Override
    public int getItemCount() {
        return movieMainItemsArrayList==null?0:movieMainItemsArrayList.size();
    }

    /**
     * 영화 데이터 메인 아이템 뷰홀더
     */
    public class MovieItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView movieTitle, movieOpenDate, moiveGrade, movieDirector;
        protected ImageView movieImage;
        protected RelativeLayout layoutMovieItem;
        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMovieItem=itemView.findViewById(R.id.layout_movie_item);
            movieTitle = itemView.findViewById(R.id.textview_movieitem_title);
            movieOpenDate = itemView.findViewById(R.id.textview_movieitem_openDate);
            moiveGrade = itemView.findViewById(R.id.textview_movieitem_grade);
            movieDirector = itemView.findViewById(R.id.textview_movieitem_director);
            movieImage = itemView.findViewById(R.id.textview_movieitem_image);
            layoutMovieItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(onItemClick!=null){
                            onItemClick.onItemClick(view,position,word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 영화 데이터 헤더 뷰홀더
     */
    public class MovieHeaderHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout layoutMovieGenre;

        public MovieHeaderHolder(@NonNull View itemView) {
            super(itemView);
            layoutMovieGenre = itemView.findViewById(R.id.layout_movie_genre);
            layoutMovieGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(onItemClick!=null){
                            onItemClick.onItemClick(v,position,word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 영화데이터 검색결과 없음 뷰홀더
     */
    public class NoResultViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvFindWord;

        public NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFindWord = itemView.findViewById(R.id.tv_find_word);
        }
    }

    /**
     * 영화 데이터 더보기 뷰홀더
     */
    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public Button btnLoadMore;
        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLoadMore=itemView.findViewById(R.id.btn_recyclerview_loadmore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(onItemClick!=null){
                            onItemClick.onItemClick(v,position,word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 메인 아이템 세팅
     *
     * @param holder   아이템 뷰홀더
     * @param position 아이템 위치값
     */
    private void showMovieItems(MovieItemViewHolder holder, int position) {
        if (movieMainItemsArrayList.get(position) != null) {
            MovieItemsVO movieItems = movieMainItemsArrayList.get(position);
            if (movieItems.getTitle() == null) {
                holder.movieTitle.setText("");
            } else {
                holder.movieTitle.setText(Html.fromHtml(movieItems.getTitle()).toString());
            }
            if (movieItems.getPubDate() == null) {
                holder.movieOpenDate.setText("");
            } else {
                holder.movieOpenDate.setText(movieItems.getPubDate() + "년");
            }
            if (movieItems.getUserRating() == null) {
                holder.moiveGrade.setText("");
            } else {
                holder.moiveGrade.setText(movieItems.getUserRating());
            }
            if (movieItems.getDirector() == null) {
                holder.movieDirector.setText("");
            } else {
                holder.movieDirector.setText(directorFilter(movieItems.getDirector()));
            }
            if (movieItems.getImgLink() == null) {
                holder.movieImage.setImageResource(R.drawable.recyclerview_errorimage);
            } else {
                Glide.with(holder.itemView)
                        .load(movieItems.getImgLink())
                        .error(R.drawable.recyclerview_errorimage)
                        .fallback(R.drawable.recyclerview_errorimage)
                        .into(holder.movieImage);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.movieImage.setClipToOutline(true);
                }
            }
        }
    }

    /**
     * 감독 데이터 필터링
     */
    private String directorFilter(String director) {
        StringBuilder sbDirector = new StringBuilder();
        String[] split = director.split("\\|");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (s != null && s.length() > 0)
                    sbDirector.append(s);

                if (i < split.length - 1)
                    sbDirector.append(", ");
            }
        }
        return sbDirector.toString();
    }

}

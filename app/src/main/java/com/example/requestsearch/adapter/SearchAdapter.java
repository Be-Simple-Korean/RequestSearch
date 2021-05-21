package com.example.requestsearch.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.requestsearch.R;
import com.example.requestsearch.network.data.book.ItemVO;
import com.example.requestsearch.network.data.movie.MovieItemsVO;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.util.Utils;

import java.util.ArrayList;

/**
 * Book & Movie 데이터 Recyclerview Adapter
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BOOK_TYPE = 0;
    public static final int MOVIE_TYPE = 1;

    public static final int FINISH_VIEW_TYPE=-1;
    public static final int BOOK_HEADER_TYPE = 0;
    public static final int BOOK_MAIN_TYPE = 3;
    public static final int NORESULT_TYPE = 1;
    public static final int LOADMORE_TYPE = 2;
    public static final int MOVIE_MAIN_TYPE = 4;
    public static final int MOVIE_HEADER_TYPE = 5;


    private ArrayList<ItemVO> detailMainItemVOArrayList; // 책 데이터 리스트
    private ArrayList<MovieItemsVO> movieItemsArrayList; // 영화 데이터 리스트
    private String word;
    private String errata = "";
    private int typeNumber = BOOK_TYPE; // 0 / 1
    private OnItemClick onItemClick = null;
    private RecyclerView recyclerView;
    private View noResultview;
    private View headerView;
    private Boolean isFinish=false;

    public SearchAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (typeNumber == BOOK_TYPE) {
            return detailMainItemVOArrayList.get(position).getViewType();
        } else {
            return movieItemsArrayList.get(position).getViewType();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case BOOK_HEADER_TYPE:
                headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_book_header, parent, false);
                return new BookHeaderViewHolder(headerView);
            case BOOK_MAIN_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_book, parent, false);
                return new BookItemViewHolder(view);
            case MOVIE_HEADER_TYPE:
                headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_movie_header, parent, false);
                return new MovieHeaderHolder(headerView);
            case MOVIE_MAIN_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_movie, parent, false);
                return new MovieItemViewHolder(view);
            case NORESULT_TYPE:
                noResultview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_noresult, parent, false);
                return new NoResultViewHolder(noResultview);
            case LOADMORE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_footer, parent, false);
                return new LoadMoreViewHolder(view);
            case FINISH_VIEW_TYPE:
                View finishView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_finish, parent, false);
                return new FinishViewHolder(finishView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        // no data, finish
//        holder.itemView.setOnClickListener(null);
//        else
//        holder.itemView.setOnClickListener();
        switch (type) {
            case NORESULT_TYPE: //결과없음 ui 처리
                noResultview.setLayoutParams(new Utils().setNoResultViewHeight(recyclerView, holder, headerView, noResultview));
                ((NoResultViewHolder) holder).tvFindWord.setText(new Utils().getSpannableData(holder, errata, onItemClick, word));
                ((NoResultViewHolder) holder).tvFindWord.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case BOOK_MAIN_TYPE:
                //메인 아이템 이벤트처리
                if (typeNumber == BOOK_TYPE) {
                    showMainItems(((BookItemViewHolder) holder), position);
                }
                break;
            case MOVIE_MAIN_TYPE:
                showMovieItems((MovieItemViewHolder) holder, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (typeNumber == BOOK_TYPE) {
            return detailMainItemVOArrayList != null ? detailMainItemVOArrayList.size() : 0;
        } else {
            return movieItemsArrayList != null ? movieItemsArrayList.size() : 0;
        }
    }

    /**
     * 책 데이터 헤더 뷰홀더
     */
    public class BookHeaderViewHolder extends RecyclerView.ViewHolder { //헤더 뷰 홀더
        protected RelativeLayout layoutMainOption;

        public BookHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMainOption = itemView.findViewById(R.id.layout_main_option);
            layoutMainOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(v, 0, word);
                    }
                }
            });
        }
    }

    /**
     * 책 데이터 메인 아이템 뷰홀더
     */
    public class BookItemViewHolder extends RecyclerView.ViewHolder { //메인 아이템 뷰홀더
        protected TextView tvBookTitle, tvBookAuthor, tvBookPublisher, tvBookPubDate, tvBookPrice;
        protected ImageView tvBookImage;
        protected RelativeLayout layoutBookItem;

        public BookItemViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutBookItem = itemView.findViewById(R.id.layout_book_item);
            tvBookTitle = itemView.findViewById(R.id.textview_bookitem_title);
            tvBookAuthor = itemView.findViewById(R.id.textview_bookitem_author);
            tvBookPublisher = itemView.findViewById(R.id.textview_bookitem_publisher);
            tvBookPubDate = itemView.findViewById(R.id.textview_bookitem_pubDate);
            tvBookPrice = itemView.findViewById(R.id.textview_bookitem_price);
            tvBookImage = itemView.findViewById(R.id.imageview_bookitem_image);
            layoutBookItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClick != null) {
                            onItemClick.onItemClick(view, position, word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 여백아이템 뷰홀더
     */
    public class FinishViewHolder extends RecyclerView.ViewHolder {

        public FinishViewHolder(@NonNull View itemView) {
            super(itemView);
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
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClick != null) {
                            onItemClick.onItemClick(v, position, word);
                        }
                    }
                }
            });
        }
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
            layoutMovieItem = itemView.findViewById(R.id.layout_movie_item);
            movieTitle = itemView.findViewById(R.id.textview_movieitem_title);
            movieOpenDate = itemView.findViewById(R.id.textview_movieitem_openDate);
            moiveGrade = itemView.findViewById(R.id.textview_movieitem_grade);
            movieDirector = itemView.findViewById(R.id.textview_movieitem_director);
            movieImage = itemView.findViewById(R.id.textview_movieitem_image);
            layoutMovieItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClick != null) {
                            onItemClick.onItemClick(view, position, word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 책 데이터 ui 세팅
     *
     * @param holder
     * @param position
     */
    private void showMainItems(BookItemViewHolder holder, int position) {
        if (detailMainItemVOArrayList.get(position) != null) {
            ItemVO items = detailMainItemVOArrayList.get(position);

            String title = TextUtils.isEmpty(items.getTitle()) ? "" : Html.fromHtml(items.getTitle()).toString();
            holder.tvBookTitle.setText(title);

            String author = TextUtils.isEmpty(items.getAuthor()) ? "" : slashDataFilter(Html.fromHtml(items.getAuthor()).toString());
            holder.tvBookAuthor.setText(author);

            String publisher = TextUtils.isEmpty(items.getPublisher()) ? "" : Html.fromHtml(items.getPublisher()).toString();
            holder.tvBookPublisher.setText(publisher);

            String date = new Utils().getDateFormat(items.getPubdate());
            holder.tvBookPubDate.setText(date);

            String price = TextUtils.isEmpty(items.getPrice()) ? "" : new Utils().getPriceFormat(items.getPrice());
            holder.tvBookPrice.setText(price);

            if (items.getImage() == null) {
//                holder.tvBookImage.setImageResource(R.drawable.recyclerview_errorimage);
                Glide.with(holder.itemView).load(R.drawable.recyclerview_errorimage);
            } else {
                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(20));
                Glide.with(holder.itemView).load(items.getImage())
                        .apply(options).into(holder.tvBookImage);

            }
        }
    }

    /**
     * 메인 아이템 세팅
     *
     * @param holder   아이템 뷰홀더
     * @param position 아이템 위치값
     */
    private void showMovieItems(MovieItemViewHolder holder, int position) {
        if (movieItemsArrayList.get(position) != null) {
            MovieItemsVO movieItems = movieItemsArrayList.get(position);

            String title = TextUtils.isEmpty(movieItems.getTitle()) ? "" : (Html.fromHtml(movieItems.getTitle()).toString());
            holder.movieTitle.setText(title);

            String pubDate = TextUtils.isEmpty(movieItems.getPubDate()) ? "" : movieItems.getPubDate() + "년";
            holder.movieOpenDate.setText(pubDate);

            String userRating = TextUtils.isEmpty(movieItems.getUserRating()) ? "" : movieItems.getUserRating();
            holder.moiveGrade.setText(userRating);

            String director = TextUtils.isEmpty(movieItems.getDirector()) ? "" : slashDataFilter(movieItems.getDirector());
            holder.movieDirector.setText(director);

            if (movieItems.getImgLink() == null) {
                holder.movieImage.setImageResource(R.drawable.recyclerview_errorimage);
            } else {
                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(20));
                Glide.with(holder.itemView).load(movieItems.getImgLink())
                        .apply(options).into(holder.movieImage);
            }
        }
    }

    /**
     * 영화데이터 검색결과 없음 뷰홀더
     */
    public class NoResultViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvFindWord;

        public NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFindWord = itemView.findViewById(R.id.textview_item_noresult);
        }
    }

    /**
     * 영화 데이터 더보기 뷰홀더
     */
    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public Button btnLoadMore;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLoadMore = itemView.findViewById(R.id.btn_recyclerview_loadmore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClick != null) {
                            onItemClick.onItemClick(v, position, word);
                        }
                    }
                }
            });
        }
    }

    /**
     * 데이터 타입 설정 = 책 = 0 / 영화 = 1
     * @param typeNumber
     */
    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    /**
     * 책 데이터 리스트 설정
     * @param detailMainItemVOArrayList
     */
    public void setDetailMainItemVOArrayList(ArrayList<ItemVO> detailMainItemVOArrayList) {
        this.detailMainItemVOArrayList = detailMainItemVOArrayList;
    }

    /**
     * 영화 데이터 리스트 설정
     */
    public void setMovieItemsArrayList(ArrayList<MovieItemsVO> movieItemsArrayList) {
        this.movieItemsArrayList = movieItemsArrayList;
    }

    /**
     * 검색 단어 설정
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 오타변환단어 설정
     * @param errata
     */
    public void setErrata(String errata) {
        this.errata = errata;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * 감독 데이터 필터링
     */
    private String slashDataFilter(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = data.split("\\|");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (s != null && s.length() > 0)
                    stringBuilder.append(s);

                if (i < split.length - 1)
                    stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

}

package com.example.requestsearch.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.requestsearch.data.book.Item;
import com.example.requestsearch.util.DateForamt;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.R;
import com.example.requestsearch.util.HeightFormat;
import com.example.requestsearch.util.PriceFormat;
import com.example.requestsearch.util.SpannableFormat;


import java.util.ArrayList;


/**
 * 책 검색결과 Recyclerview Adapter
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int NOREUSLT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;
    private static final int MAIN_TYPE = 3;


    private ArrayList<Item> detailMainItemArrayList;
    private String word;
    private String errata = "";
    private OnItemClick onItemClick = null;
    private RecyclerView recyclerView;
    private View noResultview;
    private View headerView;

    public BookAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * 단어 세팅
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 오타 변환 단어 세팅
     *
     * @param errata
     */
    public void setErrata(String errata) {
        this.errata = errata;
    }

    /**
     * 검색 데이터 아이템 리스트 세팅
     *
     * @param detailMainItemArrayList
     */
    public void setDetailMainItemArrayList(ArrayList<Item> detailMainItemArrayList) {
        this.detailMainItemArrayList = detailMainItemArrayList;
    }

    /**
     * 데이터 리스트 세팅
     */


    @Override
    public int getItemViewType(int position) {
        return detailMainItemArrayList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case NOREUSLT_TYPE: //결과없음 화면표시
                noResultview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_noresult, parent, false);
                return new NoResultViewHolder(noResultview);
            case HEADER_TYPE: //헤더표시
                headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_book_header, parent, false);
                return new HeaderViewHolder(headerView);
            case LOADMORE_TYPE: //더보기표시
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_footer, parent, false);
                return new LoadMoreViewHolder(view);
            default: //메인 아이템 표시
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_book, parent, false);
                BookItemViewHolder bookItemViewHolder = new BookItemViewHolder(view);
                return bookItemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case NOREUSLT_TYPE: //결과없음 ui 처리

                noResultview.setLayoutParams(new HeightFormat().setNoResultViewHeight(recyclerView,holder,headerView,noResultview));

                ((NoResultViewHolder) holder).tvFindWord.setText(new SpannableFormat().getSpannableData(holder,errata,onItemClick,word));
                ((NoResultViewHolder) holder).tvFindWord.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case MAIN_TYPE:
                //메인 아이템 이벤트처리
                showMainItems(((BookItemViewHolder) holder), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return detailMainItemArrayList != null ? detailMainItemArrayList.size() : 0;
    }

    /**
     * 책 데이터 더보기 뷰홀더
     */
    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public Button btnLoadMore;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLoadMore = itemView.findViewById(R.id.btn_recyclerview_loadmore);
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
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
     * 책 데이터 헤더 뷰홀더
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder { //헤더 뷰 홀더
        protected RelativeLayout layoutMainOption;

        public HeaderViewHolder(@NonNull View itemView) {
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
     * 책 데이터 결과없음 뷰홀더
     */
    public class NoResultViewHolder extends RecyclerView.ViewHolder { //결과없음 뷰 홀더
        protected TextView tvFindWord;
        protected RelativeLayout layoutNoResult;

        public NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFindWord = itemView.findViewById(R.id.textview_item_noresult);
            layoutNoResult = itemView.findViewById(R.id.layout_no_result);
        }
    }

    /**
     * 책 데이터 ui 세팅
     *
     * @param holder
     * @param position
     */
    private void showMainItems(BookItemViewHolder holder, int position) {

        if (detailMainItemArrayList.get(position) != null) {
            Item items = detailMainItemArrayList.get(position);

            String title = TextUtils.isEmpty(items.getTitle()) ? "" : Html.fromHtml(items.getTitle()).toString();
            holder.tvBookTitle.setText(title);

            String author = TextUtils.isEmpty(items.getAuthor()) ? "" : authorFilter(Html.fromHtml(items.getAuthor()).toString());
            holder.tvBookAuthor.setText(author);

            String publisher = TextUtils.isEmpty(items.getPublisher()) ? "" : Html.fromHtml(items.getPublisher()).toString();
            holder.tvBookPublisher.setText(publisher);

            String date = new DateForamt().getDateFormat(items.getPubdate());
            holder.tvBookPubDate.setText(date);

            String price = TextUtils.isEmpty(items.getPrice()) ? "" : new PriceFormat().getPriceFormat(items.getPrice());
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
     * 저자 데이터 필터링
     */
    private String authorFilter(String author) {
        StringBuilder sbDirector = new StringBuilder();
        String[] split = author.split("\\|");
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
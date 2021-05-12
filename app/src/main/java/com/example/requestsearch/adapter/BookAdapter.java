package com.example.requestsearch.adapter;

import android.text.Html;
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
import com.example.requestsearch.data.book.BookItemsVO;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 책 검색결과 Recyclerview Adapter
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int NOREUSLT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;
    private static final int MAIN_TYPE = 3;

    private ArrayList<BookItemsVO> bookMainItemsArrayList;
    //    public static ArrayList<Item> detailMainItemArrayList,
//            detailSubItemArrayList;
    //  private int number;
    private String word;
    private OnItemClick onItemClick = null;

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
     * 데이터 리스트 세팅
     *
     * @param bookMainItemsArrayList
     */
    public void setList(ArrayList<BookItemsVO> bookMainItemsArrayList) {
        this.bookMainItemsArrayList = bookMainItemsArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return bookMainItemsArrayList.get(position).getViewType();
//        if (number == 0) { //ㄱ기본
//            return bookMainItemsArrayList.get(position).getViewType();
//        }
//        else { //상세
//            return detailMainItemArrayList.get(position).getViewType();
//        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case NOREUSLT_TYPE: //결과없음 화면표시
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_noresult, parent, false);
                return new NoResultViewHolder(view);
            case HEADER_TYPE: //헤더표시
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_book_header, parent, false);
                return new HeaderViewHolder(view);
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
                ((NoResultViewHolder) holder).tvFindWord.setText(word);
                break;
            case MAIN_TYPE:
                //메인 아이템 이벤트처리
                showMainItems(((BookItemViewHolder) holder), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return bookMainItemsArrayList != null ? bookMainItemsArrayList.size() : 0;
//        if (number == 0) {
//            return bookMainItemsArrayList != null ? bookMainItemsArrayList.size() : 0;
//        }
//        else {
//            return detailMainItemArrayList != null ? detailMainItemArrayList.size() : 0;
//        }
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

        public NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFindWord = itemView.findViewById(R.id.tv_find_word);
        }
    }

    /**
     * 책 데이터 ui 세팅
     *
     * @param holder
     * @param position
     */
    private void showMainItems(BookItemViewHolder holder, int position) {
        //  if (number == 0) {
        if (bookMainItemsArrayList.get(position) != null) {
            BookItemsVO items = bookMainItemsArrayList.get(position);
            if (items.getTitle() == null) {
                holder.tvBookTitle.setText("");
            } else {
                holder.tvBookTitle.setText(Html.fromHtml(items.getTitle()).toString()); //HTML태그 제거
                if (items.getAuthor() == null) {
                    holder.tvBookAuthor.setText("");
                } else {
                    holder.tvBookAuthor.setText(Html.fromHtml(items.getAuthor()).toString());
                }
                if (items.getPublisher() == null) {
                    holder.tvBookPublisher.setText("");
                } else {
                    holder.tvBookPublisher.setText(items.getPublisher());
                }
                if (items.getPubdate() == null) {
                    holder.tvBookPubDate.setText("");
                } else {
                    SimpleDateFormat oldDataFormat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    try {
                        Date formatDate = oldDataFormat.parse(items.getPubdate());
                        String newPubDate = newDateFormat.format(formatDate);
                        holder.tvBookPubDate.setText(newPubDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (items.getPrice() == null) {
                    holder.tvBookPrice.setText("");
                } else {
                    holder.tvBookPrice.setText(
                            String.format("%,d", Integer.parseInt(items.getPrice())) //1000단위 표시
                    );
                }
                if (items.getImage() == null ) {
                    holder.tvBookImage.setImageResource(R.drawable.recyclerview_errorimage);
                } else
                    Glide.with(holder.itemView).load(items.getImage()).into(holder.tvBookImage);
            }
        }
//            else {
//                if (detailSubItemArrayList.get(position) != null) {
//                    Item items = detailMainItemArrayList.get(position);
//                    if (items.getTitle() != null) {
//                        holder.tvBookTitle.setText(Html.fromHtml(items.getTitle()).toString()); //HTML태그 제거
//                        holder.tvBookTitle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(holder.itemView.getContext(), WebViewActivty.class);
//                                intent.putExtra("url", items.getLink());
//                                holder.itemView.getContext().startActivity(intent);
//                            }
//                        });
//                    }
//                    if (items.getAuthor() != null) {
//                        holder.tvBookAuthor.setText(Html.fromHtml(items.getAuthor()).toString());
//                    } else {
//                        holder.tvBookAuthor.setText("");
//                    }
//                    if (items.getPublisher() != null) {
//                        holder.tvBookPublisher.setText(items.getPublisher());
//                    }
//                    if (items.getPubdate() != null) {
//                        SimpleDateFormat oldDataFormat = new SimpleDateFormat("yyyyMMdd");
//                        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy.MM.dd");
//                        try {
//                            Date formatDate = oldDataFormat.parse(items.getPubdate());
//                            String newPubDate = newDateFormat.format(formatDate);
//                            holder.tvBookPubDate.setText(newPubDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (items.getPrice() != null) {
//                        holder.tvBookPrice.setText(
//                                String.format("%,d", Integer.parseInt(items.getPrice())) //1000단위 표시
//                        );
//                    }
//
//                    if (items.getImage() == null || items.getImage().equals("")) {
//                        holder.tvBookImage.setImageResource(R.drawable.recyclerview_errorimage);
//                    } else
//                        Glide.with(holder.itemView).load(items.getImage()).into(holder.tvBookImage);
//                }
//            }
        // }

    }
}
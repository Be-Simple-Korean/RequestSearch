package com.example.requestsearch.data.book;


/**
 * 책 검색 데이터
 */
public class BookItemsVO {
   private String title;
   private String link;
   private String image;
   private String author;
   private String price;
   private String discount;
   private String publisher;
   private String isbn;
   private String description;
   private String pubdate;
   private int viewType = 3;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }



    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public String getPubdate() {
        return pubdate;
    }

    @Override
    public String toString() {
        return "BookItems{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", author='" + author + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", pubdate='" + pubdate + '\'' +
                '}';
    }
}

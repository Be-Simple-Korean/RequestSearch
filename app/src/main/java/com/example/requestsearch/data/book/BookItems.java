package com.example.requestsearch.data.book;


public class BookItems {
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
   private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public BookItems(String title, String link, String image, String author, String price, String discount, String publisher, String isbn, String description, String pubdate) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.author = author;
        this.price = price;
        this.discount = discount;
        this.publisher = publisher;
        this.isbn = isbn;
        this.description = description;
        this.pubdate = pubdate;
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
}

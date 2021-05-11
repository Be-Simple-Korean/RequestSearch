package com.example.requestsearch.data.detail;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item",strict = false)
public class Item {
    @Element(name = "title",required = false)
    String title;
    @Element(name = "link",required = false)
    String link;
    @Element(name = "image",required = false)
    String image;
    @Element(name = "author",required = false)
    String author;
    @Element(name = "price",required = false)
    String price;
    @Element(name = "discount",required = false)
    String discount;
    @Element(name = "publisher",required = false)
    String publisher;
    @Element(name = "isbn",required = false)
    String isbn;
    @Element(name = "description",required = false)
    String description;
    @Element(name = "pubdate",required = false)
    String pubdate;
    int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", author='" + author + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Item(String title, String link, String image, String author, String price, String discount, String publisher, String pubdate, String isbn, String description) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.author = author;
        this.price = price;
        this.discount = discount;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.isbn = isbn;
        this.description = description;
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

    public String getPubdate() {
        return pubdate;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }
}

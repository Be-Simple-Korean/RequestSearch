package com.example.requestsearch.network.data.book;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name="channel",strict = false)
public class ChannelVO {
    @Element(name="title",required = false)
    String title;
    @Element(name="link",required = false)
    String link;
    @Element(name="description",required = false)
    String description;
    @Element(name="lastBuildDate",required = false)
    String lastBuildDate;
    @Element(name="total",required = false)
    String total;
    @Element(name="start",required = false)
    String start;
    @Element(name="display",required = false)
    String display;
    @ElementList(entry="item",inline = true,required = false)
    ArrayList<ItemVO> itemVO;

    public ChannelVO() {
    }

    public ChannelVO(String lastBuildDate, String total, String start, String display, ArrayList<ItemVO> itemVO) {
        this.lastBuildDate = lastBuildDate;
        this.total = total;
        this.start = start;
        this.display = display;
        this.itemVO = itemVO;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total='" + total + '\'' +
                ", start='" + start + '\'' +
                ", display='" + display + '\'' +
                ", item=" + itemVO +
                '}';
    }


    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public String getTotal() {
        return total;
    }

    public String getStart() {
        return start;
    }

    public String getDisplay() {
        return display;
    }

    public ArrayList<ItemVO> getItemVO() {
        return itemVO;
    }
}

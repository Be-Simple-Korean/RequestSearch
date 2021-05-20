package com.example.requestsearch.data.book;

import com.example.requestsearch.data.BaseVO;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="rss",strict = false)
public class Rss extends BaseVO {
    @Element(name = "channel",required = false)
    Channel channel;

    public Rss() {
    }

    public Rss(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}

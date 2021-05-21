package com.example.requestsearch.network.data.book;

import com.example.requestsearch.network.data.BaseVO;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="rss",strict = false)
public class RssVO extends BaseVO {
    @Element(name = "channel",required = false)
    ChannelVO channelVO;

    public RssVO() {
    }

    public RssVO(ChannelVO channelVO) {
        this.channelVO = channelVO;
    }

    public ChannelVO getChannelVO() {
        return channelVO;
    }
}

package io.github.zhenbing.fgateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessage;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.04 10:37
 */
public class BaseHttpFilterChain implements HttpFilterChain {
    private HttpFilterContext head;
    private HttpFilterContext tail;


    @Override
    public HttpFilterChain addLast(HttpFilter filter) {
        HttpFilterContext node = new HttpFilterContext();
        node.setFilter(filter);

        if (head == null) {
            tail = head = node;
        } else {
            tail.addNext(node);
            tail = node;
        }
        return this;
    }

    @Override
    public void invoke(HttpMessage httpMessage, ChannelHandlerContext channelHandlerContext) {
        if (head != null) {
            head.getFilter().filter(httpMessage, channelHandlerContext, head);
        }
    }

}

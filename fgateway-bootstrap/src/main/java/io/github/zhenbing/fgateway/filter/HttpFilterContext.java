package io.github.zhenbing.fgateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessage;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.04 10:24
 */
public class HttpFilterContext {
    private HttpFilter filter;

    private HttpFilterContext next;

    public HttpFilterContext addNext(HttpFilterContext next) {
        this.next = next;
        return next;
    }

    public void setFilter(HttpFilter filter) {
        this.filter = filter;
    }

    public HttpFilter getFilter() {
        return filter;
    }

    public HttpFilterContext getNext() {
        return next;
    }

    public void invoke(HttpMessage httpMessage, ChannelHandlerContext ctx) {
        if (next != null) {
            next.getFilter().filter(httpMessage, ctx, next);
        }
    }
}

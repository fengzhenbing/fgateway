package io.github.zhenbing.fgateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMessage;

public interface HttpFilter {

    void filter(HttpMessage httpMessage, ChannelHandlerContext channelHandlerContext, HttpFilterContext httpFilterContext);

    /**
     * 是否执行该过滤器
     *
     * @return
     */
    default Boolean accept() {
        return true;
    }
}

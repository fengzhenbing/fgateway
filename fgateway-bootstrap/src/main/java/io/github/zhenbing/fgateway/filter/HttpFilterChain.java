package io.github.zhenbing.fgateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessage;

/**
 * 过滤器链
 */
public interface HttpFilterChain {

    HttpFilterChain addLast(HttpFilter filter);

    void invoke(HttpMessage httpMessage, ChannelHandlerContext channelHandlerContext);

}

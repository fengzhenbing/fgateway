package io.github.zhenbing.fgateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 11:03
 */
public abstract class AbstractHttpRequestFilter implements HttpFilter {
    public Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void filter(HttpMessage httpMessage, ChannelHandlerContext ctx, HttpFilterContext httpFilterContext) {
        filter((FullHttpRequest) httpMessage, ctx, httpFilterContext);
    }

    protected abstract void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpFilterContext httpFilterContext);
}

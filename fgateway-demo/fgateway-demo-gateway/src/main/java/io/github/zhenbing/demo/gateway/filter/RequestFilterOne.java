package io.github.zhenbing.demo.gateway.filter;

import io.github.zhenbing.fgateway.filter.AbstractHttpRequestFilter;
import io.github.zhenbing.fgateway.filter.HttpFilterContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestFilterOne extends AbstractHttpRequestFilter {
    @Override
    protected void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpFilterContext httpFilterContext) {
        logger.info("requestFilter 1");
        fullHttpRequest.headers().set("nio", "fengzhenbing");
        httpFilterContext.invoke(fullHttpRequest, ctx);
    }
}
package io.github.zhenbing.demo.gateway.filter;

import io.github.zhenbing.fgateway.filter.AbstractHttpRequestFilter;
import io.github.zhenbing.fgateway.filter.HttpFilterContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class RequestFilterTwo extends AbstractHttpRequestFilter {
    @Override
    protected void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpFilterContext httpFilterContext) {
        logger.info("requestFilter 2");
        httpFilterContext.invoke(fullHttpRequest, ctx);
    }
}

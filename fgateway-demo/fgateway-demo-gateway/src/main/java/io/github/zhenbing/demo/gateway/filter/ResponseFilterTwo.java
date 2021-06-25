package io.github.zhenbing.demo.gateway.filter;

import io.github.zhenbing.fgateway.filter.AbstractHttpResponseFilter;
import io.github.zhenbing.fgateway.filter.HttpFilterContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseFilterTwo extends AbstractHttpResponseFilter {

    @Override
    protected void filter(FullHttpResponse fullHttpResponse, ChannelHandlerContext ctx, HttpFilterContext httpFilterContext) {
        logger.info("responseFilter 2");
        httpFilterContext.invoke(fullHttpResponse, ctx);
    }

}
package io.github.zhenbing.fgateway.backend.nettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 20:59
 */
public class NettyRestClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 网关服务端的上下文
     */
    private ChannelHandlerContext fontCtx;

    /**
     * 响应调用链
     */
    private HttpFilterChain responseFilterChain;

    public NettyRestClientHandler(ChannelHandlerContext fontCtx, HttpFilterChain responseFilterChain) {
        this.fontCtx = fontCtx;
        this.responseFilterChain = responseFilterChain;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        DefaultFullHttpResponse rltRes = null;

        if (msg instanceof FullHttpResponse) {
            HttpResponse response = (FullHttpResponse) msg;

            rltRes = new DefaultFullHttpResponse(response.protocolVersion(), response.status());

            if (!response.headers().isEmpty()) {
                for (CharSequence name : response.headers().names()) {
                    for (CharSequence value : response.headers().getAll(name)) {
                        rltRes.headers().set(name, value);
                    }
                }
            }

        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            rltRes.content().writeBytes(content.content());

        }

        // 调用响应过滤链
        if (responseFilterChain != null) {
            responseFilterChain.invoke(rltRes, fontCtx);
        }

        // 写数据到前端客户端
        fontCtx.writeAndFlush(rltRes);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }



}

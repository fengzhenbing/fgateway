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

    private ResponseCallBack responseCallBack;

    public NettyRestClientHandler(ResponseCallBack responseCallBack) {
        this.responseCallBack = responseCallBack;
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

        responseCallBack.onReceive(rltRes);

        //关闭
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}

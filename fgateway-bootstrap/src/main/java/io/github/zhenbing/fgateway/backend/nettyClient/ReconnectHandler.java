package io.github.zhenbing.fgateway.backend.nettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.08 21:44
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断线了");
        ctx.fireChannelInactive();
    }

}

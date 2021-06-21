package io.github.zhenbing.fgateway.backend.nettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 20:37
 */
public class NettyRestClientInitializer extends ChannelInitializer<SocketChannel> {

    private ResponseCallBack responseCallBack;


    public NettyRestClientInitializer(ResponseCallBack responseCallBack) {
        this.responseCallBack = responseCallBack;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        //客户端http解码
        p.addLast(new HttpClientCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));

        //反压缩
        p.addLast(new HttpContentDecompressor());

        //解析结果
        p.addLast(new NettyRestClientHandler(responseCallBack));


    }

}

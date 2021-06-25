package io.github.zhenbing.fgateway.inbound;

import io.github.zhenbing.fgateway.config.FGatewayConfig;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Description 初始化添加handler
 * @Author fzb
 * @date 2020.11.03 11:21
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private HttpFilterChain httpRequestFilterChain;
    private HttpFilterChain httpResponseFilterChain;

    public HttpInboundInitializer(HttpFilterChain httpRequestFilterChain, HttpFilterChain httpResponseFilterChain) {
        this.httpRequestFilterChain = httpRequestFilterChain;
        this.httpResponseFilterChain = httpResponseFilterChain;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpOutboundHandler());
        // 添加Http编码解码器
        pipeline.addLast(new HttpServerCodec());
        //聚合 ??什么作用
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));

        //请求后端服务的客户端
        String restHttpClientName = FGatewayConfig.getConfig().getRestHttpClient();


        ChannelHandler channelHandler = new HttpInboundHandler()
                .restClient(restHttpClientName)
                .httpRequestFilterChain(httpRequestFilterChain)
                .httpResponseFilterChain(httpResponseFilterChain);

        pipeline.addLast(channelHandler);
    }

}

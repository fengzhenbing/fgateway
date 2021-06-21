package io.github.zhenbing.fgateway.backend.nettyClient;

import io.github.zhenbing.fgateway.backend.BaseRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.github.zhenbing.fgateway.backend.IRestClient;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;
import io.github.zhenbing.fgateway.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 19:57
 */
public class NettyRestClient2 implements IRestClient {

    private Logger logger = LoggerFactory.getLogger(NettyRestClient2.class);

    private HttpFilterChain responseFilterChain;

    private Bootstrap bootstrap;
    private  EventLoopGroup loopGroup;
    private static   AtomicInteger requestCnt = new AtomicInteger(0);


    private Channel channel;

    public NettyRestClient2(){

        loopGroup   = new NioEventLoopGroup(32);
        logger.info("NettyRestClient2 constructor");
    }

    @Override
    public Object request(BaseRequest frontRequest) {
        AtomicReference<Object> result = new AtomicReference<>();

        //1)
        try{
            CountDownLatch latch = new CountDownLatch(1);

            //2)构造器
            bootstrap = new  Bootstrap();
            //3)配置
            bootstrap.group(loopGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioSocketChannel.class)
                    .handler(new NettyRestClientInitializer(response -> {
                        result.set(response);
                        latch.countDown();
                    }));
            ChannelFuture channelFuture = bootstrap.connect(frontRequest.getHost(),frontRequest.getPort()).sync();
            channel = channelFuture.channel();

            //5）构造请求
            channel.writeAndFlush(((FullHttpRequest) frontRequest.getHttpRequest()).copy());
            latch.await(5, TimeUnit.SECONDS);

            //channel.closeFuture();
            logger.info("requestCnt -> {}",requestCnt.getAndIncrement());

        }catch (Exception e){
            e.printStackTrace();
        }finally {

           // loopGroup.shutdownGracefully();
        }
        return result.get();
    }



   /* public static void main(String[] args) {
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/json");
        request.headers().set(HttpHeaderNames.HOST, "localhost");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

         new NettyRestClient().request(null,request);
    }*/

}

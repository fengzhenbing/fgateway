package io.github.zhenbing.fgateway.inbound;

import cn.hutool.setting.dialect.PropsUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.BaseLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 11:08
 */
public class HttpInboundServer {

    public static final int PORT = Integer.parseInt(System.getProperty("port", PropsUtil.get("server.properties").get("gateway.port").toString()));
    private Logger logger = LoggerFactory.getLogger(HttpInboundInitializer.class);
    private HttpFilterChain httpRequestFilterChain;
    private HttpFilterChain httpResponseFilterChain;

    public void run() throws InterruptedException {
        // 1、创建EventLoopGroup
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(3);
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(1000);

        try {
            // 2、创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 3、配置启动器
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInboundInitializer(httpRequestFilterChain,httpResponseFilterChain));

            // 4、启动器启动
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            logger.info("gateway httpServer start with port : {}", PORT);

            // 5、等待服务端channel关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 6.释放资源
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    public HttpInboundServer filterChain(HttpFilterChain httpRequestFilterChain, HttpFilterChain httpResponseFilterChain) {
        this.httpRequestFilterChain = httpRequestFilterChain;
        this.httpResponseFilterChain = httpResponseFilterChain;
        return this;
    }

    public HttpInboundServer httpRequestFilterChain(HttpFilterChain httpRequestFilterChain) {
        this.httpRequestFilterChain = httpRequestFilterChain;
        return this;
    }

    public HttpInboundServer httpResponseFilterChain(HttpFilterChain httpResponseFilterChain) {
        this.httpResponseFilterChain = httpResponseFilterChain;
        return this;
    }
}

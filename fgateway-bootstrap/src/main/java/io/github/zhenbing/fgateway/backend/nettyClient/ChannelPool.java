package io.github.zhenbing.fgateway.backend.nettyClient;

 import io.netty.bootstrap.Bootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.nio.NioSocketChannel;
 import io.netty.handler.codec.http.FullHttpRequest;
 import io.netty.handler.logging.LogLevel;
 import io.netty.handler.logging.LoggingHandler;
 import io.github.zhenbing.fgateway.filter.HttpFilterChain;
 import io.github.zhenbing.fgateway.loadbalancer.Server;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.08 22:11
 */
public class ChannelPool {
    private Logger logger = LoggerFactory.getLogger(ChannelPool.class);

    private int poolSize = 1;

    private HttpFilterChain responseFilterChain;
    private ChannelHandlerContext frontCtx;
    private Server server;

    Channel  channel;

    EventLoopGroup loopGroup   = new NioEventLoopGroup(32);

    protected volatile List<Channel> channels = new CopyOnWriteArrayList<Channel>();

    private AtomicInteger index = new AtomicInteger(0);

    public ChannelPool(Server server,ChannelHandlerContext frontCtx,  HttpFilterChain responseFilterChain, int poolSize) {
        this.server = server;
        this.responseFilterChain = responseFilterChain;
        this.frontCtx = frontCtx;
         this.poolSize = poolSize;
    }

    public ChannelPool(Server server,ChannelHandlerContext frontCtx,  HttpFilterChain responseFilterChain) {
        this.server = server;
        this.responseFilterChain = responseFilterChain;
        this.frontCtx = frontCtx;
     }

    public Channel getChannnel(){
        if(channel == null || !channel.isOpen()){
             initChannel();
        }
        return channel;
    }

    /*public Channel getChannnel(){
        logger.info(" size -> {} ",channels.size());
        if(channels == null || channels.size() < poolSize){
            synchronized (this){
                Channel channel = initChannel(); // 线程安全？？
                channels.add(channel);
                return channel;
            }
        } else {
            Channel channel = channels.get(index.incrementAndGet() % poolSize);
            if(!channel.isOpen()){// 断了
                synchronized (this){
                    channels.remove(channel);
                    channel = initChannel();
                    channels.add(channel);
                    return channel;
                }

            }
            return channel;
        }
    }*/

    private Channel initChannel()  {

        logger.info(" init ");
        //2)构造器
        Bootstrap b = new  Bootstrap();

        //3)配置
        b.group(loopGroup)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                .handler(new NettyRestClientInitializer(frontCtx,responseFilterChain));

        try {
            if(channel !=null) {
                channel.close();
            }
            ChannelFuture future  = b.connect(server.getHost(),server.getPort()).sync();
            channel = future.channel();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }

        return channel;

    }

    void close() throws InterruptedException {
        /*if(channel != null){
            channel.closeFuture().sync();
        }
        if(loopGroup != null){
            loopGroup.shutdownGracefully();
        }*/
    }

}

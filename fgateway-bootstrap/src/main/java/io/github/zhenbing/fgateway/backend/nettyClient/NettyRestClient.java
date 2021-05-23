package io.github.zhenbing.fgateway.backend.nettyClient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.github.zhenbing.fgateway.backend.IRestClient;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;
import io.github.zhenbing.fgateway.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 19:57
 */
public class NettyRestClient implements IRestClient {

    private Logger logger = LoggerFactory.getLogger(NettyRestClient.class);

    private HttpFilterChain responseFilterChain;

    private  AtomicInteger requestCnt = new AtomicInteger(0);

    private ChannelPool channelPool;





    @Override
    public void request(ChannelHandlerContext frontCtx, FullHttpRequest frontRequest, ILoadBalancer loadBalancer) {
        //负载均衡
        Server backendServer = loadBalancer.chooseServer(frontRequest);
        String url =backendServer.getScheme()+"://"+ backendServer.getHost()+":"+backendServer.getPort()+frontRequest.uri();
        if(logger.isDebugEnabled()){
            logger.debug("请求后端服务地址为:\n{}",url);
        }

        //1)
         try{
             if(channelPool == null){
                 synchronized (this){
                     if(channelPool ==null){
                         channelPool = new ChannelPool(backendServer,frontCtx,responseFilterChain);
                     }

                 }

             }

             Channel channel =  channelPool.getChannnel();

             //5）构造请求
             channel.writeAndFlush(frontRequest.copy());


             logger.info("requestCnt -> {}",requestCnt.getAndIncrement());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
    }



    @Override
    public void registerResponseChain(HttpFilterChain responseFilterChain) {
        this.responseFilterChain = responseFilterChain;
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

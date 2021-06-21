package io.github.zhenbing.fgateway.backend.nettyClient;

import io.github.zhenbing.fgateway.backend.BaseRequest;
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


    private  AtomicInteger requestCnt = new AtomicInteger(0);

    private ChannelPool channelPool;





    @Override
    public Object request(BaseRequest baseRequest) {

        //1)
         try{
             if(channelPool == null){
                 synchronized (this){
                     if(channelPool ==null){
                         channelPool = new ChannelPool(null,null,null);
                     }

                 }

             }

             Channel channel =  channelPool.getChannnel();

             //5）构造请求
             channel.writeAndFlush(((FullHttpRequest) baseRequest.getHttpRequest()).copy());


             logger.info("requestCnt -> {}",requestCnt.getAndIncrement());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }

         return null;
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

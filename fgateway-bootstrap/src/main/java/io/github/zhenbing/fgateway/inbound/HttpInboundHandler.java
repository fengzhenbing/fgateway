package io.github.zhenbing.fgateway.inbound;

import cn.hutool.setting.dialect.PropsUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import io.github.zhenbing.fgateway.backend.IRestClient;
import io.github.zhenbing.fgateway.backend.nettyClient.NettyRestClient2;
import io.github.zhenbing.fgateway.backend.okHttpClient.OkHttpClientRestClient;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.BaseLoadBalancer;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Description http请求处理
 * @Author fzb
 * @date 2020.11.03 11:38
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);

    public static final IRestClient DEFAULT_REST_CLIENT = new NettyRestClient2();

    private IRestClient restClient = DEFAULT_REST_CLIENT;

    private ILoadBalancer loadBalancer = new BaseLoadBalancer().rule(PropsUtil.get("server.properties").get("gateway.loadbalancer.rule").toString());

    private HttpFilterChain httpRequestFilterChain;
    private HttpFilterChain httpResponseFilterChain;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        try {
            //1）调用请求过滤链
            if (httpRequestFilterChain != null) {
                httpRequestFilterChain.invoke(fullRequest, ctx);
            }

            //2）注册响应过滤链
            if (httpResponseFilterChain != null) {
                restClient.registerResponseChain(httpResponseFilterChain);
            }

            //3 请求后端服务
            restClient.request(ctx, fullRequest, loadBalancer);

            // handleTest(ctx,fullRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    private void handleTest(ChannelHandlerContext ctx, FullHttpRequest fullRequest) {
        String uri = fullRequest.uri();
        logger.info(uri);
        ByteBuf content = Unpooled.buffer();
        FullHttpResponse fullHttpResponse = null;
        try {
            content.writeBytes("你好".getBytes("UTF-8"));
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            HttpUtil.setContentLength(fullHttpResponse, fullHttpResponse.content().readableBytes());
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");

        } catch (Exception e) {
            logger.error("处理接口出错", e);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {

            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.writeAndFlush(fullHttpResponse);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public HttpInboundHandler restClient(String name) {
        if ("OKHttpClient".equals(name)) {
            this.restClient = new OkHttpClientRestClient();
        } else {
            this.restClient = DEFAULT_REST_CLIENT;
        }
        return this;
    }

    public HttpInboundHandler httpRequestFilterChain(HttpFilterChain filterChain) {
        this.httpRequestFilterChain = filterChain;
        return this;
    }

    public HttpInboundHandler httpResponseFilterChain(HttpFilterChain filterChain) {
        this.httpResponseFilterChain = filterChain;
        return this;
    }

}

package io.github.zhenbing.fgateway.backend.okHttpClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import kotlin.Pair;
import okhttp3.*;
import io.github.zhenbing.fgateway.backend.IRestClient;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;
import io.github.zhenbing.fgateway.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Description
 * @Author fzb
 * @date 2020.10.28 18:25
 */
public class OkHttpClientRestClient implements IRestClient {
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
            .build();

    private Logger logger = LoggerFactory.getLogger(OkHttpClientRestClient.class);

    private HttpFilterChain responseFilterChain;

    @Override
    public void request(ChannelHandlerContext ctx, FullHttpRequest request, ILoadBalancer loadBalancer) {
        //负载均衡
        Server backendServer = loadBalancer.chooseServer(request);
        String url = backendServer.getScheme() + "://" + backendServer.getHost() + ":" + backendServer.getPort() + request.uri();
        if(logger.isDebugEnabled()){
            logger.debug("请求后端服务地址为:\n{}", url);
        }

        Request backendRequest = new Request.Builder()
                .url(url)
                .build();
        client.newCall(backendRequest).enqueue(new Callback() {
            //失败回调
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            //成功回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    doWriteResponse(ctx, request, response);
                }

            }
        });
    }

    public void doWriteResponse(ChannelHandlerContext ctx, FullHttpRequest fullRequest, Response backendResponse) {
        ByteBuf content = Unpooled.buffer();
        FullHttpResponse fullHttpResponse = null;
        try {
            content.writeBytes(backendResponse.body().string().getBytes(CharsetUtil.UTF_8));

            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            HttpUtil.setContentLength(fullHttpResponse, fullHttpResponse.content().readableBytes());

            //设置头
            Iterator<Pair<String, String>> it = backendResponse.headers().iterator();
            while (it.hasNext()) {
                Pair<String, String> headItem = it.next();
                fullHttpResponse.headers().set(headItem.component1(), headItem.component2());
            }

        } catch (Exception e) {
            logger.error("处理接口出错", e);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                //1)调用响应的过滤链
                if (responseFilterChain != null) {
                    responseFilterChain.invoke(fullHttpResponse, ctx);
                }

                //2）写数据到前端客户端
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
    public void registerResponseChain(HttpFilterChain responseFilterChain) {
        this.responseFilterChain = responseFilterChain;
    }
}

package io.github.zhenbing.fgateway.backend;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;
import io.netty.handler.codec.http.HttpRequest;

public interface IRestClient {
    Object request(BaseRequest request);

}

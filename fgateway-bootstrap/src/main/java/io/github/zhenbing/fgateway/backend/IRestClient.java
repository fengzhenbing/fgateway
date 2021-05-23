package io.github.zhenbing.fgateway.backend;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.loadbalancer.ILoadBalancer;

public interface IRestClient {
    void request(ChannelHandlerContext ctx, FullHttpRequest request, ILoadBalancer loadBalancer);

    void registerResponseChain(HttpFilterChain responseFilterChain);
}

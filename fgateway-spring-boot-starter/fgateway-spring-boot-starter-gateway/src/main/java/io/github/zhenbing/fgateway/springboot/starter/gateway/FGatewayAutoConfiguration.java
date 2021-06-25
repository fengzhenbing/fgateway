package io.github.zhenbing.fgateway.springboot.starter.gateway;

import io.github.zhenbing.fgateway.GatewayBootstrap;
import io.github.zhenbing.fgateway.config.FGatewayConfig;
import io.github.zhenbing.fgateway.filter.AbstractHttpRequestFilter;
import io.github.zhenbing.fgateway.filter.AbstractHttpResponseFilter;
import io.github.zhenbing.fgateway.filter.BaseHttpFilterChain;
import io.github.zhenbing.fgateway.filter.HttpFilterChain;
import io.github.zhenbing.fgateway.inbound.HttpInboundServer;
import io.github.zhenbing.fgateway.springboot.starter.gateway.config.GatewayConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class FGatewayAutoConfiguration {

    @Bean
    public NettyServerStartListener nettyServerStartListener(final HttpInboundServer httpInboundServer){
        return new NettyServerStartListener(httpInboundServer);
    }

    @Bean
    public HttpInboundServer httpInboundServer(final GatewayConfig gatewayConfig,
                                               final List<AbstractHttpRequestFilter> httpRequestFilterList,
                                               final List<AbstractHttpResponseFilter> httpResponseFilterList){
        // 配置
        FGatewayConfig.getConfig()
                .setPort(gatewayConfig.getPort())
                .setBackendUrls(gatewayConfig.getBackendUrls())
                .setLoadbalancerRule(gatewayConfig.getLoadbalancerRule())
                .setRestHttpClient(gatewayConfig.getRestHttpClient());

        //配置请求过滤链
        HttpFilterChain requestFilterChain = new BaseHttpFilterChain();
        Optional.ofNullable(httpRequestFilterList).ifPresent(list->{
            list.stream().forEach(requestFilterChain::addLast);
        });

        //配置响应过滤链
        HttpFilterChain responseFilterChain = new BaseHttpFilterChain();
        Optional.ofNullable(httpResponseFilterList).ifPresent(list->{
            list.stream().forEach(responseFilterChain::addLast);
        });

        //运行
        HttpInboundServer server = new HttpInboundServer().filterChain(requestFilterChain,responseFilterChain);
        return server;
    }

    @Bean
    @ConfigurationProperties(prefix = "fgateway")
    public GatewayConfig gatewayConfig(){
        return new GatewayConfig();
    }
}

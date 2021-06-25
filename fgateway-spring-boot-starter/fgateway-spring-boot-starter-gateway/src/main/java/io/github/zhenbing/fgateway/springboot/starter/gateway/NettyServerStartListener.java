package io.github.zhenbing.fgateway.springboot.starter.gateway;

import io.github.zhenbing.fgateway.inbound.HttpInboundServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class NettyServerStartListener implements ApplicationListener<ContextRefreshedEvent> {
    private final HttpInboundServer httpInboundServer;

    public NettyServerStartListener(final HttpInboundServer httpInboundServer) {
        this.httpInboundServer = httpInboundServer;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.httpInboundServer.run();
    }
}

package io.github.zhenbing.fgateway.springboot.starter.gateway.config;

import lombok.Data;

import java.util.List;

@Data
public class GatewayConfig {
    private Integer port;
    private List<String> backendUrls;
    private String restHttpClient;
    private String loadbalancerRule;
}

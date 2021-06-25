package io.github.zhenbing.fgateway.config;

import java.util.List;

public class FGatewayConfig {
    private Integer port;
    private List<String> backendUrls;
    private String restHttpClient;
    private String loadbalancerRule;

    private static FGatewayConfig config = new FGatewayConfig();

    public static FGatewayConfig getConfig(){
        return config;
    }

    public Integer getPort() {
        return port;
    }

    public FGatewayConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public List<String> getBackendUrls() {
        return backendUrls;
    }

    public FGatewayConfig setBackendUrls(List<String> backendUrls) {
        this.backendUrls = backendUrls;
        return this;
    }

    public String getRestHttpClient() {
        return restHttpClient;
    }

    public FGatewayConfig setRestHttpClient(String restHttpClient) {
        this.restHttpClient = restHttpClient;
        return this;
    }

    public String getLoadbalancerRule() {
        return loadbalancerRule;
    }

    public FGatewayConfig setLoadbalancerRule(String loadbalancerRule) {
        this.loadbalancerRule = loadbalancerRule;
        return this;
    }

}

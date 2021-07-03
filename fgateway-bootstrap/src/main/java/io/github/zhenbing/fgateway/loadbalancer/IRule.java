package io.github.zhenbing.fgateway.loadbalancer;

public interface IRule {
    Server choose(Object key);

    void setLoadBalancer(ILoadBalancer lb);

    ILoadBalancer getLoadBalancer();
}

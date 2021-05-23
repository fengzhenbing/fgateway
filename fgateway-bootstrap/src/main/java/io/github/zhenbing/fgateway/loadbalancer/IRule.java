package io.github.zhenbing.fgateway.loadbalancer;

public interface IRule {
    public Server choose(Object key);

    public void setLoadBalancer(ILoadBalancer lb);

    public ILoadBalancer getLoadBalancer();
}

package io.github.zhenbing.fgateway.loadbalancer;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.04 00:01
 */
public abstract class AbstractRule implements IRule {
    private ILoadBalancer loadBalancer;

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.loadBalancer = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return loadBalancer;
    }
}

package io.github.zhenbing.fgateway.loadbalancer;

import java.util.List;

public interface ILoadBalancer {

    public void addServers(List<Server> newServers);

    public Server chooseServer(Object key);

    public List<Server> getAllServers();
}

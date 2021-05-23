package io.github.zhenbing.fgateway.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 轮询
 * @Author fzb
 * @date 2020.11.03 23:52
 */
public class RoundRobinRule extends AbstractRule {

    private volatile AtomicInteger serverIndex = new AtomicInteger(-1);

    @Override
    public Server choose(Object key) {
        List<Server> serverList = getLoadBalancer().getAllServers();
        int index = serverIndex.incrementAndGet() % serverList.size();
        return serverList.get(index);
    }

}

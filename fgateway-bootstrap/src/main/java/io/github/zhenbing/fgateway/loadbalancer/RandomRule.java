package io.github.zhenbing.fgateway.loadbalancer;

import java.util.List;

/**
 * @Description 随机
 * @Author fzb
 * @date 2020.11.03 23:53
 */
public class RandomRule extends AbstractRule {

    @Override
    public Server choose(Object key) {
        List<Server> serverList = getLoadBalancer().getAllServers();
        int index = (int) (Math.random() * serverList.size());
        return serverList.get(index);
    }

}

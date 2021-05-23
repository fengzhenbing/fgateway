package io.github.zhenbing.fgateway.loadbalancer;

import cn.hutool.setting.dialect.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 23:50
 */
public class BaseLoadBalancer implements ILoadBalancer {

    private Logger logger = LoggerFactory.getLogger(BaseLoadBalancer.class);

    private final static IRule DEFAULT_RULE = new RoundRobinRule();

    private IRule rule = DEFAULT_RULE;

    // private List<Server> serverList = Collections.synchronizedList(new ArrayList<>());

    // 读多写少用
    private List<Server> serverList = new CopyOnWriteArrayList<>();

    public BaseLoadBalancer() {
        String urls = PropsUtil.get("server.properties").get("gateway.backendUrls").toString();
        if (urls == null || urls.isEmpty()) {
            String error = "gateway.backendUrls is not configured!";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
        String[] urlArr = urls.split(",");
        for (String url : urlArr) {
            String[] us = url.split("://");
            if (us.length == 2) {
                String schema = us[0];
                String[] us2 = us[1].split(":");
                if (us2.length == 2) {
                    serverList.add(new Server(schema, us2[0], Integer.parseInt(us2[1])));
                }
            }
        }

    }

    @Override
    public void addServers(List<Server> newServers) {
        serverList.addAll(newServers);
    }

    @Override
    public Server chooseServer(Object key) {
        if (null == rule.getLoadBalancer()) {
            rule.setLoadBalancer(this);
        }
        return rule.choose(key);
    }

    @Override
    public List<Server> getAllServers() {
        return serverList;
    }

    public BaseLoadBalancer rule(String name) {
        if ("Random".equals(name)) {
            this.rule = new RandomRule();
        } else {
            this.rule = DEFAULT_RULE;
        }
        return this;
    }

}

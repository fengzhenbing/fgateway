package io.github.zhenbing.fgateway.route;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description
 * @Author fzb
 * @date 2021.07.03 23:28
 */
public class ConditionDefinition {
    private String name;

    private Map<String, String> args = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "ConditionDefinition{" +
                "name='" + name + '\'' +
                ", args=" + args +
                '}';
    }
}

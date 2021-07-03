package io.github.zhenbing.fgateway.route;

import io.github.zhenbing.fgateway.filter.FilterDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author fzb
 * @date 2021.07.03 22:35
 */
public class RouteDefinition {
    private String id;
    private ServerPath serverPath;
    private List<ConditionDefinition> conditionDefinitionList;
    private List<FilterDefinition> filterDefinitionList;
    private Map<String, Object> metadata = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServerPath getServerPath() {
        return serverPath;
    }

    public void setServerPath(ServerPath serverPath) {
        this.serverPath = serverPath;
    }

    public List<ConditionDefinition> getConditionDefinitionList() {
        return conditionDefinitionList;
    }

    public void setConditionDefinitionList(List<ConditionDefinition> conditionDefinitionList) {
        this.conditionDefinitionList = conditionDefinitionList;
    }

    public List<FilterDefinition> getFilterDefinitionList() {
        return filterDefinitionList;
    }

    public void setFilterDefinitionList(List<FilterDefinition> filterDefinitionList) {
        this.filterDefinitionList = filterDefinitionList;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "RouteDefinition{" +
                "id='" + id + '\'' +
                ", serverPath=" + serverPath +
                ", conditionDefinitionList=" + conditionDefinitionList +
                ", filterDefinitionList=" + filterDefinitionList +
                ", metadata=" + metadata +
                '}';
    }
}

package io.github.zhenbing.fgateway.route;

import java.util.List;

public interface RouteDefinitionRepository {

    List<RouteDefinition> getRouteDefinitions();

    void save(RouteDefinition route);

    void delete(String routeId);
}

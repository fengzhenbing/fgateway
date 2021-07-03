package io.github.zhenbing.fgateway.route;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author fzb
 * @date 2021.07.03 23:36
 */
public class ServerPath {
    private String protocol;
    private String host;
    private Integer port;
    private String uri;
    private String method;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ServerPath{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}

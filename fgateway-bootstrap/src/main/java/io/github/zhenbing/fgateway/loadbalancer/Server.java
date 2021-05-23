package io.github.zhenbing.fgateway.loadbalancer;

/**
 * @Description
 * @Author fzb
 * @date 2020.11.03 23:34
 */
public class Server {

    private String scheme = "http";

    private int port;

    private String host;

    Server(String scheme, String host, int port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}

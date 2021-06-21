package io.github.zhenbing.fgateway.backend.nettyClient;

@FunctionalInterface
public interface ResponseCallBack {
    void onReceive(Object response);
}

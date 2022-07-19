package com.warnder.registry;

import java.net.InetSocketAddress;

public abstract class ServiceRegistry {

    /**
     * 注册服务到注册中心
     *
     * @param rpcServiceName    完整的服务名称
     * @param inetSocketAddress 远程服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {

    }
}

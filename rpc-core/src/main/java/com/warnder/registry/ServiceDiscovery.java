package com.warnder.registry;

import java.net.InetSocketAddress;

public abstract class ServiceDiscovery {
    /**
     * 根据 rpcServiceName 获取远程服务地址
     *
     * @param rpcServiceName 完整的服务名称（class name）
     * @return 远程服务地址
     */
    InetSocketAddress lookupService(String rpcServiceName) {
        return null;
    }
}

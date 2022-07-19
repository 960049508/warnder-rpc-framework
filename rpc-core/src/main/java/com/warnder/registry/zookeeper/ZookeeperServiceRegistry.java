package com.warnder.registry.zookeeper;

import com.warnder.registry.ServiceRegistry;
import com.warnder.registry.zookeeper.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

public class ZookeeperServiceRegistry extends ServiceRegistry {
    public static void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, rpcServiceName, inetSocketAddress);
    }
}

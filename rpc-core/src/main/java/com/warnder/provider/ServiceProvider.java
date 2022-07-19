package com.warnder.provider;

import com.warnder.common.enumeration.RpcError;
import com.warnder.common.exception.RpcException;
import com.warnder.common.utils.Constants;
import com.warnder.registry.zookeeper.ZookeeperServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceProvider {
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>(); // (接口全类名，实现类)
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet(); // 实现类全类名

    private static synchronized <T> void addService(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) return; // 该服务已经注册过了
        registeredService.add(serviceName); // 该服务还没注册过
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            // 某个接口只能有一个对象提供服务，如果重复注册同一接口的服务，后面的覆盖前面的，可以加上其他信息（版本号）解决这个问题
            serviceMap.put(i.getCanonicalName(), service);
        }
        log.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    public static synchronized Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

    public static synchronized <T> void publishService(T service,String host, int port) {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            addService(service);
            ZookeeperServiceRegistry.registerService(service.getClass().getCanonicalName(), new InetSocketAddress(host, port));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}

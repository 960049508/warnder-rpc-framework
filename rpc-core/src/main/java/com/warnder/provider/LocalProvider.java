package com.warnder.provider;

import com.warnder.common.enumeration.RpcError;
import com.warnder.common.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalProvider {

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>(); // (接口全类名，实现类)
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet(); // 实现类全类名

    public static synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) return; // 该服务已经注册过了
        registeredService.add(serviceName); // 该服务还没注册过
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            // 注册的服务未实现接口，抛出异常
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            // 某个接口只能有一个对象提供服务，如果重复注册同一接口的服务，后面的覆盖前面的，可以加上其他信息（版本号）解决这个问题
            serviceMap.put(i.getCanonicalName(), service);
        }
        log.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(), serviceName);
    }

    public static synchronized Object getService(String interfaceName) {
        Object service = serviceMap.get(interfaceName);
        if (service == null) {
            // 找不到对应服务，抛出异常
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

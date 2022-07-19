package com.warnder.transport.socket.client;


import com.warnder.common.entity.RpcRequest;
import com.warnder.common.entity.RpcResponse;
import com.warnder.transport.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient socketClient = new SocketClient(); // 代理对象新建客户端，发送接收消息
        return ((RpcResponse<?>) socketClient.sendRequest(rpcRequest, host, port)).getData();
    }
}

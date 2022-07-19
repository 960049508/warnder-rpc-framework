package com.warnder.transport.socket.server;

import com.warnder.common.entity.RpcRequest;
import com.warnder.common.entity.RpcResponse;
import com.warnder.handler.InvocationHandler;
import com.warnder.provider.LocalProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketHandlerThread implements Runnable {

    private Socket socket;

    public SocketHandlerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject(); // 接收的消费者消息
            String interfaceName = rpcRequest.getInterfaceName(); // 获得接口名
            Object service = LocalProvider.getService(interfaceName); // 从本地注册中获得该接口对应的服务
            Object result = InvocationHandler.invocation(rpcRequest, service); // 交给requestHandler调用服务
            // TODO:尚不支持返回失败消息
            objectOutputStream.writeObject(RpcResponse.success(result)); // 通过输出流向客户端发送响应消息
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }
}

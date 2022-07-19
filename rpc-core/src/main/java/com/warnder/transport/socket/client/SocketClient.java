package com.warnder.transport.socket.client;


import com.warnder.common.entity.RpcRequest;
import com.warnder.transport.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketClient implements RpcClient {

    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        // 1.创建Socket对象并且指定服务器的地址和端口号
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 输出流写入发送的消息 RPCRequest
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            // 输入流接受消息 RPCResponse
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用时有错误发生：", e);
            return null;
        }
    }
}

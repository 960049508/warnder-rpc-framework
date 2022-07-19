package com.warnder.test.diffTest.socket;


import com.warnder.provider.LocalProvider;
import com.warnder.test.apiImpl.GreetServiceImpl;
import com.warnder.transport.RpcServer;
import com.warnder.transport.socket.server.SocketServer;

public class TestSocketServer {
    public static void main(String[] args) {
        GreetServiceImpl service = new GreetServiceImpl();
        LocalProvider.register(service);
        RpcServer socketServer = new SocketServer();
        socketServer.start(9001);
    }
}

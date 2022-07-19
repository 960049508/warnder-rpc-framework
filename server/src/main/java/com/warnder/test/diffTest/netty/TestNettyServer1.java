package com.warnder.test.diffTest.netty;


import com.warnder.provider.ServiceProvider;
import com.warnder.test.apiImpl.GreetServiceImpl;
import com.warnder.transport.RpcServer;
import com.warnder.transport.netty.server.NettyServer;

public class TestNettyServer1 {
    public static void main(String[] args) {
        GreetServiceImpl service = new GreetServiceImpl();
        ServiceProvider.publishService(service,"127.0.0.1",8081);
        RpcServer nettyServer = new NettyServer();
        nettyServer.start(8081);
    }
}

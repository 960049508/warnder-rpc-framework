package com.warnder.test.diffTest.netty;


import com.warnder.api.greet.GoodbyeService;
import com.warnder.api.transformParma.TransformObject;
import com.warnder.transport.netty.client.RpcProxy;

public class TestNettyClient {
    public static void main(String[] args) {
        RpcProxy proxy = new RpcProxy("127.0.0.1", 8080);
        GoodbyeService goodbyeService = proxy.getProxy(GoodbyeService.class);
        TransformObject object = new TransformObject(1, "This is client1");
        String res = goodbyeService.sayGoodbye(object);
        System.out.println(res);
        while (true) ;
    }
}

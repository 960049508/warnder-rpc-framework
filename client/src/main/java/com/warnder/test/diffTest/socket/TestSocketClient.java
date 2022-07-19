package com.warnder.test.diffTest.socket;


import com.warnder.api.greet.HelloService;
import com.warnder.api.transformParma.TransformObject;
import com.warnder.transport.socket.client.RpcProxy;

public class TestSocketClient {
    public static void main(String[] args) {
        RpcProxy proxy = new RpcProxy("127.0.0.1", 9001);
        HelloService helloService = proxy.getProxy(HelloService.class);
        TransformObject object = new TransformObject(0, "This is client");
        String res = helloService.sayHello(object);
        System.out.println(res);
        while (true) ;
    }
}

package com.warnder.test.apiImpl;


import com.warnder.api.greet.GoodbyeService;
import com.warnder.api.greet.HelloService;
import com.warnder.api.transformParma.TransformObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GreetServiceImpl implements HelloService, GoodbyeService {
    private static final Logger logger = LoggerFactory.getLogger(GreetServiceImpl.class);

    @Override
    public String sayHello(TransformObject object) {
        logger.info("sayHello:" + object.toString());
        return object.toString();
    }

    @Override
    public String sayGoodbye(TransformObject object) {
        logger.info("sayGoodbye:" + object.toString());
        return object.toString();
    }
}

package com.warnder.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.warnder.common.entity.RpcRequest;
import com.warnder.common.entity.RpcResponse;
import com.warnder.common.enumeration.SerializerType;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 两个特点，一是基于字节的序列化，对空间利用率较高，在网络传输时可以减小体积；二是序列化时记录属性对象的类型信息，这样在反序列化时就不会出现之前的问题了。
 */
@Slf4j
public class KryoSerializer implements Serializer {

    // Kryo 可能存在线程安全问题，文档上是推荐放在 ThreadLocal 里，一个线程一个 Kryo。
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        //kryo.setReferences(true);
        //kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        // 在序列化时，先创建一个 Output 对象（Kryo 框架的概念），
        // 接着使用 writeObject 方法将对象写入 Output 中，
        // 最后调用 Output 对象的 toByte() 方法即可获得对象的字节数组。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = kryoThreadLocal.get();
        kryo.writeObject(output, obj);
        kryoThreadLocal.remove();
        return output.toBytes();
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        // 反序列化则是从 Input 对象中直接 readObject，这里只需要传入对象的类型，而不需要具体传入每一个属性的类型信息。
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = kryoThreadLocal.get();
        Object o = kryo.readObject(input, clazz);
        kryoThreadLocal.remove();
        return o;
    }

    @Override
    public int getCode() {
        return SerializerType.KRYO_SERIALIZER.getCode();
    }
}

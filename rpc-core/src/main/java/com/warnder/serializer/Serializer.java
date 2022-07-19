package com.warnder.serializer;


import com.warnder.common.enumeration.SerializerType;

/**
 * 序列化接口
 */
public interface Serializer {
    static Serializer getByCode(int code) {
        //TODO 优化这段if-else语句，学习并优化序列化器
        if (code == SerializerType.KRYO_SERIALIZER.getCode()) {
            return new KryoSerializer();
        } else if (code == SerializerType.JSON_SERIALIZER.getCode()) {
            return new JsonSerializer();
        } else if (code == SerializerType.JDK_SERIALIZER.getCode()) {
            return new JdkSerializer();
        } else if (code == SerializerType.HESSIAN_SERIALIZER.getCode()) {
            return new HessianSerializer();
        } else if (code == SerializerType.PROTOSTUFF_SERIALIZER.getCode()) {
            return new ProtobufSerializer();
        } else {
            return new KryoSerializer();
        }
    }

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}

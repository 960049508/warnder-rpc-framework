package com.warnder.serializer;

import com.warnder.common.enumeration.SerializerType;
import com.warnder.common.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 使用JDK的序列化器
 */
@Slf4j
public class JdkSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("序列化时有错误发生:" + e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("序列化时有错误发生:" + e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerType.JDK_SERIALIZER.getCode();
    }
}

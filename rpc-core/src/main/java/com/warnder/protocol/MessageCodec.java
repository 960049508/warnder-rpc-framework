package com.warnder.protocol;

import com.warnder.common.entity.RpcRequest;
import com.warnder.common.entity.RpcResponse;
import com.warnder.common.enumeration.PackageType;
import com.warnder.common.enumeration.RpcError;
import com.warnder.common.exception.RpcException;
import com.warnder.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageCodec extends ByteToMessageCodec {
    // 1. 魔数 4字节
    private static final int MAGIC_NUMBER = 0xFACDEBDE;
    // 2. 消息类型 4字节
    // PackageType.REQUEST_PACK.getCode()
    // 3. 序列化算法 4字节
    private Serializer serializer;
    // 4. 消息长度 4字节
    // 5. 请求序号  版本号  ？

    public MessageCodec(Serializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        // 消息类型
        if (o instanceof RpcRequest) {
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        } else if (o instanceof RpcResponse) {
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 序列化算法
        byteBuf.writeInt(serializer.getCode());
        // 消息序列化
        byte[] serializeBytes = serializer.serialize(o);
        // 消息长度
        byteBuf.writeInt(serializeBytes.length);
        // 消息体
        byteBuf.writeBytes(serializeBytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        // 验证魔数
        int magic = byteBuf.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error(RpcError.UNKNOWN_PROTOCOL + ": {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        // 消息类型
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error(RpcError.UNKNOWN_PACKAGE_TYPE + ":{}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        // 序列化算法
        int serializerCode = byteBuf.readInt();
        Serializer serializer = Serializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error(RpcError.UNKNOWN_SERIALIZER + ": {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        // 消息长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        // 消息反序列化
        Object obj = serializer.deserialize(bytes, packageClass);
        list.add(obj);
    }
}

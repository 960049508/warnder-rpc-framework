package com.warnder.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerType {
    KRYO_SERIALIZER(0, "KRYO"),
    JSON_SERIALIZER(1, "JSON"),
    JDK_SERIALIZER(2, "JDK"),
    HESSIAN_SERIALIZER(3, "HESSIAN"),
    PROTOSTUFF_SERIALIZER(4, "protostuff");
    private final int code;
    private final String value;
}

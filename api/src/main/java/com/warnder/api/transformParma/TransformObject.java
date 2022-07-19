package com.warnder.api.transformParma;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TransformObject implements Serializable {
    private Integer id;
    private String message;

    // 使用jackson序列化，必须要有无参构造函数
    public TransformObject() {
    }
}

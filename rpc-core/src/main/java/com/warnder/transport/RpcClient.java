package com.warnder.transport;


import com.warnder.common.entity.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest, String host, int port);
}

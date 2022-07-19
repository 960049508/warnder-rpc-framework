package com.warnder.transport.netty.server;


import com.warnder.common.entity.RpcRequest;
import com.warnder.common.entity.RpcResponse;
import com.warnder.handler.InvocationHandler;
import com.warnder.provider.ServiceProvider;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        Object service = ServiceProvider.getService(rpcRequest.getInterfaceName());
        Object result = InvocationHandler.invocation(rpcRequest,service);
        ChannelFuture future = channelHandlerContext.writeAndFlush(RpcResponse.success(result));
        future.addListener(ChannelFutureListener.CLOSE);
        // 当这个ByteBuf对象的引用计数值为0时，表示此对象可回收。手动释放。
        ReferenceCountUtil.release(rpcRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}

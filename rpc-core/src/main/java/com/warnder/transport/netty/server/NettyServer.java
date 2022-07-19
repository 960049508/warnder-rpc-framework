package com.warnder.transport.netty.server;

import com.warnder.protocol.MessageCodec;
import com.warnder.serializer.ProtobufSerializer;
import com.warnder.transport.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer implements RpcServer {
    @Override
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
                    //.option(ChannelOption.SO_BACKLOG, 256)
                    // 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
                    //.option(ChannelOption.SO_KEEPALIVE, true)
                    // 禁用nagle算法 （解决TCP黏包方案1）
                    //.childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 对入站\出站事件进行日志记录
                            //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            //TODO 生成序列化器实例与服务器/客户端代码解耦，能从配置文件中获取序列化器对象
                            pipeline.addLast(new MessageCodec(new ProtobufSerializer()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // future.sync() 会等待异步事件执行完成，并且返回自身；在这里等待异步的 socket 绑定事件完成
            ChannelFuture future = serverBootstrap.bind(port).sync();
            // 同步阻塞监听future的关闭事件，防止finally的语句块被触发导致关闭netty服务
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

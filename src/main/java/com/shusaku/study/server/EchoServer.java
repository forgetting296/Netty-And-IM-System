package com.shusaku.study.server;

import com.shusaku.study.handler.EchoHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.ServerSocket;

public class EchoServer {

    private final ServerBootstrap b = new ServerBootstrap();

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void runServer() {
        EventLoopGroup bossLoop = new NioEventLoopGroup(1);
        EventLoopGroup workLoop = new NioEventLoopGroup();

        try {

            b.group(bossLoop,workLoop);
            b.localAddress(port);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.option(ChannelOption.SO_KEEPALIVE, true);

            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(EchoHandler.echoHandlerInstance);
                }
            });

            ChannelFuture future = b.bind().sync();

            ChannelFuture close = future.channel().close();

            close.sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer(8888).runServer();
    }
}

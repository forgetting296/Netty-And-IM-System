package com.shusaku.study.server;

import com.shusaku.study.handler.NettyDiscardHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-12 16:16
 */
public class NettyDiscardServer {

    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public NettyDiscardServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public void runServer() {

        //创建反应器线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workLoopGroup = new NioEventLoopGroup();

        try {
            //设置反应器线程组
            b.group(bossLoopGroup,workLoopGroup);
            //设置通道类型
            b.channel(NioServerSocketChannel.class);
            //设置监听端口
            b.localAddress(serverPort);
            //设置通道参数
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //装配流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyDiscardHandler());
                }

            });

            //绑定服务器 阻塞　　直到绑定成功
            ChannelFuture future = b.bind().sync();

            //等待通道关闭的异步任务结束
            //服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = future.channel().closeFuture();

            closeFuture.sync();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭EvenvLoopGroup
            //释放线程组资源　　包括创建的线程
            workLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        new NettyDiscardServer(8888).runServer();

    }

}


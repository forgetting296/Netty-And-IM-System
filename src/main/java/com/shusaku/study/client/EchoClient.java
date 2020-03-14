package com.shusaku.study.client;

import com.shusaku.study.handler.EchoClientHandler;
import com.shusaku.study.handler.EchoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.PrinterMoreInfoManufacturer;
import java.util.Scanner;

public class EchoClient {

    Logger logger = LoggerFactory.getLogger(EchoClient.class);

    private int port;
    private String serverIp;

    Bootstrap b = new Bootstrap();

    public EchoClient(int port, String serverIp) {
        this.port = port;
        this.serverIp = serverIp;
    }

    public void runClient() {
        NioEventLoopGroup workLoop = new NioEventLoopGroup();

        try {
            b.group(workLoop);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(serverIp,port);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(EchoClientHandler.clientHandlerInstance);
                }
            });
            ChannelFuture channelFuture = b.connect();
            channelFuture.addListener((ChannelFuture futureListener) -> {
                if (futureListener.isSuccess()) {
                    logger.info("EchoClient 客户端连接成功！");
                } else {
                    logger.info("EchoClient 客户端连接失败！");
                }
            });

            //阻塞 直到连接成功
            channelFuture.sync();
            Channel channel = channelFuture.channel();
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入发送内容：");
            while(scanner.hasNext()) {
                String next = scanner.next();
                byte[] bytes = (System.currentTimeMillis() + " >> " + next).getBytes();
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(bytes);
                channel.writeAndFlush(buffer);
                System.out.println("请输入发送内容：");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workLoop.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient(8888,"127.0.0.1").runClient();
    }
}

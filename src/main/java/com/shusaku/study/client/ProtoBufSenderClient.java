package com.shusaku.study.client;

import com.shusaku.study.protocol.Msg;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-12 14:53
 */
public class ProtoBufSenderClient {

    Logger logger = LoggerFactory.getLogger(ProtoBufSenderClient.class);

    static String content = "我会是株千年不凋的雨凇　生长在炽热的南国　等待一场飞雪";
    public static Msg buildMsg(int id, String content) {

        Msg.Builder builder = Msg.newBuilder();
        builder.setId(id);
        builder.setContent(content);
        Msg build = builder.build();
        return build;
    }

    public void runClient() throws InterruptedException {

        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        //设置反应器线程组
        b.group(loopGroup);
        //设置通道类型
        b.channel(NioServerSocketChannel.class);
        //设置监听端口
        b.remoteAddress("127.0.0.1",8888);
        //设置通道参数
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        //装配通道流水线
        b.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //再将二进制字节流　添加length  改成Header－Content形式　　防止半包和粘包问题
                socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                //先转为二进制　　
                socketChannel.pipeline().addLast(new ProtobufEncoder());
            }
        });

        ChannelFuture f = b.connect();

        f.sync();

        Channel channel = f.channel();

        for(int i = 0;i < 1000;i ++) {
            channel.writeAndFlush(buildMsg(i,i + " -> " + content));
            logger.info("发送报文数: " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ProtoBufSenderClient().runClient();
    }

}

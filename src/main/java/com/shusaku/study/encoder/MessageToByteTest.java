package com.shusaku.study.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.junit.Test;

import java.util.List;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-11 11:59
 */
public class MessageToByteTest {

    @Test
    public void test() {

        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new InBoundTest());
                channel.pipeline().addLast(new OutBoundTest());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0;j < 100;j ++) {
            ByteBuf buffer = Unpooled.buffer();
            //buffer.writeInt(j);
            //channel.writeInbound(buffer);
            channel.write(j);
        }

        channel.flush();//如果没有flush　readOutBound拿不到数据　　只能打印出站处理器中的内容　　即只走在出站处理器中得操作

        ByteBuf buf = channel.readOutbound();
        while (buf != null) {
            System.out.println(buf.readInt());
            buf = channel.readOutbound();
        }

    }

}

class OutBoundTest extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(integer);
        System.out.println("out: " + integer);
    }
}

class InBoundTest extends ByteToMessageDecoder implements ChannelInboundHandler {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int i = byteBuf.readInt();
        System.out.println("in: " + i);
        list.add(i);
    }
}

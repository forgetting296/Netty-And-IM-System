package com.shusaku.study.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.junit.Test;

import java.util.List;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-11 14:05
 */
public class StringToIntegerTest {

    @Test
    public void testStringToInteger() {
        try {
            ChannelInitializer i = new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new Integer2Byte());//注意　此处是出站处理器　顺序是反着的　所以先将Integer2Byte先加入到管道中
                    channel.pipeline().addLast(new String2Integer());
                }
            };

            EmbeddedChannel channel = new EmbeddedChannel(i);
            for(int j = 0;j < 10;j ++) {
                String s = "i am " + j;
                channel.write(s);
            }

            channel.flush();

            ByteBuf buf = channel.readOutbound();
            while(buf != null) {
                System.out.println("change to byte: " + buf.readByte());
                buf = channel.readOutbound();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class String2Integer extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {

        //list.add(Integer.valueOf(s));
        char[] chars = s.toCharArray();
        for(char c: chars) {
            if(c >= 48 && c <= 57) {
                Integer num = Integer.valueOf(String.valueOf(c));
                System.out.println("encode String to Integer: " + num);
                list.add(num);
            }
        }
    }
}

class Integer2Byte extends MessageToByteEncoder<Integer> implements ChannelOutboundHandler {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf byteBuf) throws Exception {
        System.out.println("encode integer to byte: " + integer);
        byteBuf.writeByte(integer);
    }
}

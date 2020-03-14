package com.shusaku.study.decoder;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @program: Java8Test
 * @description: ReployingDecoder　一般用于数据解析的简单逻辑场景
 * @author: Shusaku
 * @create: 2020-03-10 16:55
 */
public class StringDecoder {

    public static void main(String[] args) {
        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new StringReplayingDecoder());
                channel.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for(int j = 0;j < 100;j ++) {
            int randomInt = RandomUtil.randomInt(1, 3);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(randomInt * bytes.length);
            for(int k = 0;k < randomInt;k ++){
                buffer.writeBytes(bytes);
            }
            channel.writeInbound(buffer);
        }
    }

}

class StringProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }
}

class StringReplayingDecoder extends ReplayingDecoder<StringReplayingDecoder.Status> {

    private int length;
    private byte[] bytes;

    enum Status{
        PARSE_1,PARSE_2;
    }

    public StringReplayingDecoder() {
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (state()) {
            case PARSE_1:

                length = byteBuf.readInt();
                bytes = new byte[length];
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:

                byteBuf.readBytes(bytes);
                list.add(new String(bytes,"utf-8"));
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}

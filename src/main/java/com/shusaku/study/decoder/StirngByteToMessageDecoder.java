package com.shusaku.study.decoder;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-10 17:30
 */
public class StirngByteToMessageDecoder {

    public static void main(String[] args) {
        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new StringProcessDecoder());
                channel.pipeline().addLast(new StringProcessHandler());
                //channel.pipeline().addLast(new Frame);
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

class StringProcessDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        //消息头确定　
        if(byteBuf.readableBytes() < 4) {
            return;
        }
        //消息头已经完成
        //真正的消息　从缓冲区读取数据之前　先设置回滚点
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        if(byteBuf.readableBytes() < length){
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        list.add(new String(bytes,"utf-8"));
    }
}

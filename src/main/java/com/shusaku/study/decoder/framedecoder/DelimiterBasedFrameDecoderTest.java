package com.shusaku.study.decoder.framedecoder;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 * @program: Java8Test
 * @description: 不仅支持换行符　　还可以支持其他类型的分隔符
 * @author: Shusaku
 * @create: 2020-03-11 10:48
 */
public class DelimiterBasedFrameDecoderTest {

    public static void main(String[] args) {

        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";
        String spliter = "\t";
        ByteBuf deLimiter = Unpooled.copiedBuffer(spliter.getBytes(Charset.forName("utf-8")));
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,true,deLimiter));
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringFrameProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for(int j = 0;j < 100;j ++) {
            int random = RandomUtil.randomInt(1, 3);
            ByteBuf buffer = Unpooled.buffer();
            //buffer.writeInt(random * bytes.length);
            for(int k = 0;k < random;k ++) {
                buffer.writeBytes(bytes);
            }
            buffer.writeBytes(spliter.getBytes(Charset.forName("utf-8")));
            channel.writeInbound(buffer);
        }
    }

}

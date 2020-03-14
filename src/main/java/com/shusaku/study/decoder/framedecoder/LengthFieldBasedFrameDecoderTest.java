package com.shusaku.study.decoder.framedecoder;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-11 11:08
 */
public class LengthFieldBasedFrameDecoderTest {

    @Test
    public void testHeaderContentLengthFieldBasedFrameDecoder() {

        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";
        //参数意义　从索引０的位置开始　　长度字段所占字节数　因为是ｉｎｔ所以为４　长度矫正值　舍弃字节数　包括长度字段所占的字节　以及要舍弃的正文字节
        final LengthFieldBasedFrameDecoder splitter = new LengthFieldBasedFrameDecoder(1024,0,4,0,7);
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(splitter);
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringFrameProcessHandler());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);
        for(int j = 0;j < 100;j ++) {
            int random = RandomUtil.randomInt(1, 3);
            ByteBuf buffer = Unpooled.buffer();
            //buffer.writeInt(random * bytes.length);
            String s = "第" + j + "次发送: " + content;
            byte[] bytes = s.getBytes(Charset.forName("utf-8"));
            buffer.writeInt(bytes.length); //这个的字节长度对应最后要丢弃的字节的长度　　因为不是正文
            buffer.writeBytes(bytes);       //正文的字节   但是最终输出的文本内容　要根据LengthFieleBasedFrameDecoder的参数决定
            channel.writeInbound(buffer);
        }

    }

    @Test
    public void testMoreFieldHeaderContentLengthFieldBasedFrameDecoder () {

        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";
        final int version = 100;
        //参数意义　从索引０的位置开始　　长度字段所占字节数　因为是ｉｎｔ所以为４　长度矫正值这里因为多发送了一个version　char类型　所以长度矫正值为２　舍弃字节数　包括长度字段所占的字节　以及要舍弃的正文字节
        final LengthFieldBasedFrameDecoder splitter = new LengthFieldBasedFrameDecoder(1024,0,4,2,0);
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(splitter);
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringFrameProcessHandler());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);
        for(int j = 0;j < 100;j ++) {
            int random = RandomUtil.randomInt(1, 3);
            ByteBuf buffer = Unpooled.buffer();
            //buffer.writeInt(random * bytes.length);
            String s = "第" + j + "次发送: " + content;
            byte[] bytes = s.getBytes(Charset.forName("utf-8"));
            buffer.writeInt(bytes.length); //这个的字节长度对应最后要丢弃的字节的长度　　因为不是正文
            buffer.writeChar(version);
            buffer.writeBytes(bytes);       //正文的字节   但是最终输出的文本内容　要根据LengthFieleBasedFrameDecoder的参数决定
            channel.writeInbound(buffer);
        }

    }

}

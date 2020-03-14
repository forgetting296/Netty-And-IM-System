package com.shusaku.study.decoder.framedecoder;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-11 09:52
 */
public class LineBasedFrameDecoder {

    public static void main(String[] args) {

        ChannelInitializer i = new ChannelInitializer() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new io.netty.handler.codec.LineBasedFrameDecoder(1024));
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringFrameProcessHandler());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);

        String content = "夕边倦看收天幕，崇岭尽眺原无乡 ";

        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for(int j = 0;j < 100;j ++) {
            int random = RandomUtil.randomInt(1, 3);
            ByteBuf buffer = Unpooled.buffer();
            //buffer.writeInt(random * bytes.length);
            for(int k = 0;k < random;k ++) {
                buffer.writeBytes(bytes);
            }
            buffer.writeBytes("\r\n".getBytes(Charset.forName("utf-8")));
            channel.writeInbound(buffer);
        }

    }

}

class StringFrameProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }
}

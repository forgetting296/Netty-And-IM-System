package com.shusaku.study.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2020-03-10 16:11
 */
public class ReplayingDecoderTest {

    public static void main(String[] args) {

        ChannelInitializer init = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new IntegerAddReplayingDecoder());
                channel.pipeline().addLast(new IntegerProcessHandler());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(init);

        for(int i = 0;i < 10;i ++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            channel.writeInbound(buffer);
        }

    }



}

class IntegerAddReplayingDecoder extends ReplayingDecoder<IntegerAddReplayingDecoder.Status> {

    private int first;
    private int second;

    public IntegerAddReplayingDecoder() {
        super(Status.PARSE1);
    }

    enum Status{
        PARSE1,PARS2
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        /*int i = byteBuf.readInt();
        System.out.println("解码出一个整数：　" + i);
        list.add(i);*/

        switch (state()) {
            case PARSE1:
                first = byteBuf.readInt();
                checkpoint(Status.PARS2);
                break;
            case PARS2:
                second = byteBuf.readInt();
                list.add(first + second);
                checkpoint(Status.PARSE1);
                break;
                default:
                    break;
        }

    }
}

class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println((Integer) msg);
    }
}

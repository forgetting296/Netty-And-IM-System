package com.shusaku.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

@ChannelHandler.Sharable
public class EchoClientHandler extends ChannelOutboundHandlerAdapter {

    public static final EchoClientHandler clientHandlerInstance = new EchoClientHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.getBytes(0,bytes);

        //释放Bytebuf的两种方法
        buf.release();

        //第二种方法
        //super.channelRead(ctx,msg);
    }
}

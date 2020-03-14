package com.shusaku.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(NettyDiscardHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        try {
            logger.info("收到消息,丢弃如下：　");
            while(buf.isReadable()) {
                System.out.print((char) buf.readByte());
            }
            System.out.println();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.shusaku.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

@ChannelHandler.Sharable//标识  一个Handler实例 可以被多个通道安全的共享  即 多个通道的流水线可以加入同一个Handler业务处理器
public class EchoHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(EchoHandler.class);

    public static final EchoHandler echoHandlerInstance = new EchoHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        logger.info("msg type: " + (buf.hasArray() ? "堆内存" : "直接内存"));
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.getBytes(0,bytes);
        logger.info("server received: " + new String(bytes, Charset.forName("UTF-8")));
        logger.info("写回前: msg.refCnt" + ((ByteBuf) msg).refCnt());

        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        channelFuture.addListener((ChannelFuture futureListener) -> {
            logger.info("写回后: msg.refCnt " + ((ByteBuf) msg).refCnt());
        });
    }
}

package com.shusaku.common.codec;

import com.shusaku.common.ProtoInstant;
import com.shusaku.common.bean.msg.ProtoMsg;
import com.shusaku.common.exception.InvalidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtoBufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //标记一下当前读的指针位置
        byteBuf.markReaderIndex();
        //判断包头长度  魔数Short 2个字节  版本号Short 2个字节  正文长度int 4个字节
        if(byteBuf.readableBytes() < 8) {
            return;
        }

        short magic = byteBuf.readShort();
        if(magic != ProtoInstant.MAGIC_CODE) {
            //魔数可以理解为通信口令  如果魔术不对应  出于安全考虑  抛出异常  中止通信
            throw new InvalidFrameException("客户端口令不对： " + channelHandlerContext.channel().remoteAddress());
        }

        //读取版本号
        short versionCode = byteBuf.readShort();
        //读取正文长度
        int length = byteBuf.readInt();

        if(length < 0) {
            //非法数据 关闭连接
            channelHandlerContext.close();
        }

        if(length > byteBuf.readableBytes()) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] array;
        if(byteBuf.hasArray()) {
            //堆缓冲
            array = byteBuf.slice().array();
        } else {
            //直接缓冲
            array = new byte[length];
            byteBuf.readBytes(array,0,length);
        }

        ProtoMsg.Message message = ProtoMsg.Message.parseFrom(array);

        if(message != null) {
            list.add(message);
        }

    }
}

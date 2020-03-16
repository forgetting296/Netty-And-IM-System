package com.shusaku.common.codec;

import com.shusaku.common.ProtoInstant;
import com.shusaku.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtoBufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg.Message message, ByteBuf byteBuf) throws Exception {

        //Header-Content模式  在Heander中  写入 魔数、版本号、正文长度  Content中写入正文
        byteBuf.writeShort(ProtoInstant.MAGIC_CODE);
        byteBuf.writeShort(ProtoInstant.VERSION_CODE);

        //将对象转换为字节
        byte[] bytes = message.toByteArray();
        //获取要发送的正文的长度
        int length = bytes.length;
        //正文长度  防止半包问题
        byteBuf.writeInt(length);
        //正文内容的字节数组
        byteBuf.writeBytes(bytes);
    }

}

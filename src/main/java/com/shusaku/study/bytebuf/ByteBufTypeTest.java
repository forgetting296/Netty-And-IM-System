package com.shusaku.study.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-13 15:12
 */
public class ByteBufTypeTest {

    private final Logger logger = LoggerFactory.getLogger(ByteBufTypeTest.class);

    final static Charset UTF_8 = Charset.forName("utf-8");

    public static void main(String[] args) {

        //取得堆内存
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        buffer.writeBytes("异乡人　独孤客".getBytes(UTF_8));

        if(buffer.hasArray()) {
            byte[] array = buffer.array();
            int offset = buffer.arrayOffset() + buffer.readerIndex();
            int length = buffer.readableBytes();
            System.out.println("******************************");
            System.out.println(new String(array,offset,length,UTF_8));
            System.out.println("******************************");
        } else {
            System.out.println("no has array");
        }
        buffer.release();
    }

    public void testDirectBuffer() {
        Charset charset = Charset.forName("UTF-8");
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer();
        directBuffer.writeBytes("异乡人　独孤客".getBytes(charset));
        if(!directBuffer.hasArray()) {

            int readableBytes = directBuffer.readableBytes();
            byte[] bytes = new byte[readableBytes];
            directBuffer.readBytes(bytes);
            logger.info(new String(bytes,charset));
        }

        directBuffer.release();
    }

    public void testHeapBuffer() {
        Charset charset = Charset.forName("UTF-8");
        ByteBuf heapBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        heapBuf.writeBytes("异乡人　独孤客".getBytes(charset));
        if(heapBuf.hasArray()) {
            int length = heapBuf.readableBytes();
            byte[] array = heapBuf.array();
            int offset = heapBuf.readerIndex() + heapBuf.arrayOffset();
            logger.info(new String(array,offset,length,charset));
        }
        heapBuf.release();
    }

    //我曹为什么最后一个字会乱码
    public void compositeBufTest() {

        Charset charset = Charset.forName("UTF-8");

        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();

        ByteBuf bufHeader = Unpooled.copiedBuffer("叹希奇", charset);
        ByteBuf bufBody = Unpooled.copiedBuffer("封剑主", charset);

        compositeByteBuf.addComponents(bufHeader,bufBody);
        sendMessage(compositeByteBuf);

        bufHeader.retain();
        compositeByteBuf.release();

        compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        ByteBuf bufBody2 = Unpooled.copiedBuffer("意轩邈", charset);
        compositeByteBuf.addComponents(bufHeader,bufBody2);
        sendMessage(compositeByteBuf);

        compositeByteBuf.release();
    }

    public void sendMessage(CompositeByteBuf compositeByteBuf) {
        //compositeByteBuf.
        for(ByteBuf b : compositeByteBuf.decompose(0,compositeByteBuf.maxNumComponents())) {
            int length = b.readableBytes();
            byte[] bytes = new byte[length];
            b.getBytes(b.readerIndex(),bytes);
            System.out.println(new String(bytes,Charset.forName("UTF-8")));
            System.out.println();
        }
    }

    public void compositeBufTest2() {

        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer(3);

        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{1,2,3}));
        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{5,6}));

        //合并成一个缓冲区  参数是开始索引和取合并后的字节数组的长度
        ByteBuffer buffer = compositeByteBuf.nioBuffer(0, 4);

        byte[] array = buffer.array();

        for (byte b : array) {
            System.out.print(b);
        }
        compositeByteBuf.release();
    }

}

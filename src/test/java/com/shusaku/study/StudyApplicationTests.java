package com.shusaku.study;

import com.shusaku.study.protocol.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@SpringBootTest
class StudyApplicationTests {

    final Logger logger = LoggerFactory.getLogger(StudyApplicationTests.class);

    public static Msg buildMsg() {

        Msg.Builder builder = Msg.newBuilder();
        builder.setId(1);
        builder.setContent("夕边倦看收天幕，崇岭尽眺原无乡");
        Msg build = builder.build();
        return build;
    }

    @Test
    public void serndDesr() throws IOException {

        Msg.Builder builder = Msg.newBuilder();

        builder.setContent("征尘幂　孤峰披");
        builder.setId(1);//Msg.getDefaultInstance();

        Msg build = builder.build();
        byte[] data = build.toByteArray();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data);

        data = outputStream.toByteArray();
        Msg msg = Msg.parseFrom(data);
        System.out.println("id: = " + msg.getId());
        System.out.println("content: = " + msg.getContent());
    }

    @Test
    public void serndDesr2() throws IOException {

        Msg msg = buildMsg();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        msg.writeTo(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Msg message = Msg.parseFrom(inputStream);

        System.out.println("id: " + message.getId());
        System.out.println("content: " + message.getContent());

    }

    @Test
    public void serAndDesr3() throws IOException {
        //第三种序列化与反序列化方式　　可以解决半包、粘包问题
        Msg msg = buildMsg();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        msg.writeDelimitedTo(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Msg message = Msg.parseDelimitedFrom(inputStream);

        System.out.println("id: " + message.getId());
        System.out.println("content: " + message.getContent());
    }

    @Test
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

    @Test
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

    @Test  //我曹为什么最后一个字会乱码
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

    @Test
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

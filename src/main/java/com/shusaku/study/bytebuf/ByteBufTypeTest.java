package com.shusaku.study.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-13 15:12
 */
public class ByteBufTypeTest {

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

}

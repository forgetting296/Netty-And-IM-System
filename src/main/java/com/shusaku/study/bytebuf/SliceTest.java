package com.shusaku.study.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-13 16:53
 */
public class SliceTest {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("动作：　分配 buffer(9, 100)",buffer);

        buffer.writeBytes(new byte[]{1,2,3,4});
        print("动作：　写入四个字节byte[]{1,2,3,4}", buffer);

        //会抛异常　　索引越界
        //ByteBuf slice = buffer.slice(0,111);
        //正常　　capacity writerIndex readableBytes 与参数二相同
        ByteBuf slice = buffer.slice(0,9);
        //ByteBuf slice = buffer.slice();
        print("切片slice",slice);

        ByteBuf duplicate = buffer.duplicate();
        print("切片duplicate",duplicate);
    }

    private static void print(String desc, ByteBuf buf) {
        System.out.println(desc);
        System.out.println("isReadable: " + buf.isReadable());
        System.out.println("readerIndex: " + buf.readerIndex());
        System.out.println("readableBytes: " + buf.readableBytes());
        System.out.println("isWritable: " + buf.isWritable());
        System.out.println("writerIndex: " + buf.writerIndex());
        System.out.println("capacity: " + buf.capacity());
        System.out.println("maxCapacity: " + buf.maxCapacity());
        System.out.println("maxWriteableBytes: " + buf.maxWritableBytes());
    }

}

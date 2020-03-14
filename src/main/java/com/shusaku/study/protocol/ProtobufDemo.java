package com.shusaku.study.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @program: crazy-netty
 * @description:
 * @author: Shusaku
 * @create: 2020-03-12 10:46
 */
public class ProtobufDemo {

    public static Msg buildMsg() {
        Msg.Builder builder = Msg.newBuilder();
        builder.setId(1);
        builder.setContent("夕边倦看收天幕，崇岭尽眺原无乡");
        Msg build = builder.build();
        return build;
    }

    public static void main(String[] args) throws IOException {
        Msg message = buildMsg();
        //将Ｍｓｇ对象序列化成字节数组
        byte[] bytes = message.toByteArray();
        //可以用于网络传输　　保存到内存或者外存
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(bytes);

        bytes = outputStream.toByteArray();

        Msg msg = Msg.parseFrom(bytes);

        System.out.println("id: = " + msg.getId());
        System.out.println("content: = " + msg.getContent());
    }

}

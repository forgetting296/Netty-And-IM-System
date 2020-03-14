package com.shusaku.study.protocol;

import java.io.ByteArrayInputStream;
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

    public void serndDesr2() throws IOException {

        Msg msg = buildMsg();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        msg.writeTo(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Msg message = Msg.parseFrom(inputStream);

        System.out.println("id: " + message.getId());
        System.out.println("content: " + message.getContent());

    }

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

}

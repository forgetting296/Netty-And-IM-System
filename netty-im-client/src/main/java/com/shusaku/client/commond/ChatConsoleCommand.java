package com.shusaku.client.commond;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Data
@Service("ChatConsoleCommand")
public class ChatConsoleCommand implements BaseCommand {

    private String toUserId;
    private String message;
    public static final String KEY = "2";

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请按格式输入聊天信息 id:message");
        String[] strings;
        while(true) {
            String next = scanner.next();
            strings = next.split(":");
            if(strings.length != 2) {
                System.out.println("请按格式输入聊天信息 id:message");
            } else {
                break;
            }
        }
        toUserId = strings[0];
        message = strings[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "聊天";
    }
}

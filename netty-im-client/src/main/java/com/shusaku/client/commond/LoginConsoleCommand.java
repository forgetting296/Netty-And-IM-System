package com.shusaku.client.commond;

import com.google.common.base.Splitter;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Data
@Service("LoginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand {

    public static final String KEY = "1";

    private String username;
    private String password;

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入用户信息username:password    ");
        String[] strings;
        while(true) {
            String input = scanner.next();
            //List<String> strings = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(input);
            strings = input.split(":");
            if(strings.length != 2) {
                System.out.println("请按照正确格式输入 username:password");
            }else{
                break;
            }
        }
        username = strings[0];
        password = strings[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }
}

package com.shusaku.client.commond;

import java.util.Scanner;

public interface BaseCommand {
    void exec(Scanner scanner);

    String getKey();

    String getTip();
}

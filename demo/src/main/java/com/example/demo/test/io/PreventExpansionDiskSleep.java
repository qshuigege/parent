package com.example.demo.test.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class PreventExpansionDiskSleep {

    public static void main(String[] args) {
        int count = 0;
        while (count < 10) {
            try {
                FileWriter fileWriter = new FileWriter(new File("D:/PreventExpansionDiskSleep.txt"), true);//每次都覆盖之前的内容
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.append(new Date().toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                count++;
            }

            try {
                Thread.sleep(1000 * 60 * 19);//每隔19分钟写磁盘
            } catch (InterruptedException e) {
                count++;
            }
        }
    }

}

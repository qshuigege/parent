package com.example.demo.test.win_cmd;

import java.io.*;

public class TestWinCmd {

    public static void main(String[] args) throws IOException {
        Process exec = Runtime.getRuntime().exec("wmic memorychip");
        InputStream in = exec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gbk"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        reader.close();
        exec.destroy();
        System.out.println(sb);

    }

}

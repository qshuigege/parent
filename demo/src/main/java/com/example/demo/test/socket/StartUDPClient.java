package com.example.demo.test.socket;

import java.util.Scanner;

public class StartUDPClient {

    public static void main(String[] args) {
        try {
            UDPSocketClient instance = new UDPSocketClient("localhost", 12306);
            while (true) {
                Scanner input = new Scanner(System.in);
                System.out.print("请输入想要发送的数据：");
                String data = input.nextLine();
                if ("886".equals(data)){
                    break;
                }
                instance.send(data.getBytes());
            }
            instance.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}





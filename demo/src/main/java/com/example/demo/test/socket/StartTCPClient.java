package com.example.demo.test.socket;

import java.util.Scanner;

public class StartTCPClient {

    public static void main(String[] args) {
        try {
            UDPSocketClient instance = UDPSocketClient.getInstanceReusable("localhost", 12306);
            while (true) {
                Scanner input = new Scanner(System.in);
                System.out.print("请输入想要发送的数据：");
                String data = input.nextLine();
                instance.send(data.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}





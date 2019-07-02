package com.example.demo.test.socket;

import java.util.Scanner;

public class StartUDPClient {

    public static void main(String[] args) {
        try {
            while (true) {
                Scanner input = new Scanner(System.in);
                String data = input.nextLine();
                UDPSocketClient.getInstance("localhost", 12306).send(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}





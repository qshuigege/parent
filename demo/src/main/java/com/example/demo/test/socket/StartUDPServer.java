package com.example.demo.test.socket;

import java.io.IOException;
import java.net.*;

public class StartUDPServer {

    public static void main(String[] args) {
        try {
            UDPSocketServer.getInstance(12306).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}





package com.example.demo.test.socket;

import java.io.IOException;
import java.util.Scanner;

public class StartTCPServer {

    public static void main(String[] args) throws IOException {
        TCPSocketServer instance = TCPSocketServer.getInstance(2233);
        instance.start();
    }

}





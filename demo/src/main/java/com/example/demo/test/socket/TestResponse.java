package com.example.demo.test.socket;

public class TestResponse {

    public static void main(String[] args) throws Exception{
        UDPSocketServer instance = UDPSocketServer.getInstance(2300);
        instance.start();
    }

}

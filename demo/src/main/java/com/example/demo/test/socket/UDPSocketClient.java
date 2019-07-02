package com.example.demo.test.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocketClient{
    private DatagramSocket client;
    private String serverIp;
    private int serverPort;

    private UDPSocketClient(String serverIp, int serverPort) throws SocketException {
        this.client = new DatagramSocket();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public static UDPSocketClient getInstance(String serverIp, int serverPort) throws SocketException{
        return new UDPSocketClient(serverIp, serverPort);
    }

    public void send(String data) throws IOException {
        byte[] by = data.getBytes();
        DatagramPacket dp = new DatagramPacket(by, by.length, InetAddress.getByName(serverIp), serverPort);
        client.send(dp);
        client.close();
    }

}
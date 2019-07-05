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

    public UDPSocketClient(String serverIp, int serverPort) throws SocketException {
        this.client = new DatagramSocket();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    /*public static UDPSocketClient getInstance(String serverIp, int serverPort) throws SocketException{
        return new UDPSocketClient(serverIp, serverPort);
    }*/

    public void close() {
        if (!this.client.isClosed()) {
            this.client.close();
        }
    }


    public void send(byte[] data) throws IOException {
        DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(serverIp), serverPort);
        client.send(dp);
    }

}
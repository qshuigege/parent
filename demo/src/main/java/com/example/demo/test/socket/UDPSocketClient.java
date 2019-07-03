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
    private boolean isreusable = false;

    private UDPSocketClient(String serverIp, int serverPort) throws SocketException {
        this.client = new DatagramSocket();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public static UDPSocketClient getInstance(String serverIp, int serverPort) throws SocketException{
        return new UDPSocketClient(serverIp, serverPort);
    }

    public static UDPSocketClient getInstanceReusable(String serverIp, int serverPort) throws SocketException{
        UDPSocketClient udpSocketClient = new UDPSocketClient(serverIp, serverPort);
        udpSocketClient.isreusable = true;
        return udpSocketClient;
    }

    public void close() {
        if (!this.client.isClosed()) {
            this.client.close();
        }
    }


    public void send(byte[] data) throws IOException {
        if (client.isClosed()) {
            return;
        }
        //byte[] by = data.getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(serverIp), serverPort);
        client.send(dp);
        if (!isreusable) {
            client.close();
        }
    }

}
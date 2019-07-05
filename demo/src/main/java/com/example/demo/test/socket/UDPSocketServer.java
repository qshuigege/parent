package com.example.demo.test.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPSocketServer{
    private DatagramSocket server;
    private boolean isStarted = false;
    private short maxReceiveSize;

    private UDPSocketServer(int port, short maxReceiveSize) throws SocketException {
        if (maxReceiveSize<10){
            maxReceiveSize = 10;
        }
        this.maxReceiveSize = maxReceiveSize;
        server = new DatagramSocket(port);
    }

    public static UDPSocketServer getInstance(int port, short maxReceiveSize) throws SocketException{
        return new UDPSocketServer(port, maxReceiveSize);
    }

    public void start(UDPServerTask task) throws IOException {
        if (isStarted){
            return;
        }
        isStarted = true;
        while (true){
            byte[] b = new byte[maxReceiveSize];
            DatagramPacket dp = new DatagramPacket(b, b.length);
            server.receive(dp);
            new Thread(new UDPServerRunnable(dp, task)).start();
        }
    }

    public void close(){
        server.close();
    }

    private static class UDPServerRunnable implements Runnable{
        private DatagramPacket packet;
        private UDPServerTask task;
        private UDPServerRunnable(DatagramPacket packet, UDPServerTask task){
            this.packet = packet;
            this.task = task;
        }

        @Override
        public void run() {
            task.execute(packet);
        }
    }

    public interface UDPServerTask{
        void execute(DatagramPacket datagramPacket);
    }
}
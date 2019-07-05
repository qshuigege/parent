package com.example.demo.test.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketClient {

    private Socket clientSocket;
    private boolean isClosed = false;

    private TCPSocketClient(String serverIP, int serverPort) throws IOException {
        this.clientSocket = new Socket(serverIP, serverPort);
    }

    public static TCPSocketClient getInstance(String serverIP, int serverPort) throws IOException {
        return new TCPSocketClient(serverIP, serverPort);
    }

    public String send(byte[] data) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        //outputStream.write("我是Balla_兔子".getBytes());
        //byte[] bytes = new byte[]{0x44, 0x52,0x00,0x00,0x2b,0x03,0x00,0x00,0x00,0x03,0x01,0x00,0x19,(byte)0xe1};
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        //outputStream.write(data);
        bufferedWriter.write(new String(data));
        bufferedWriter.write("\n");
        //outputStream.flush();
        bufferedWriter.flush();
        //clientSocket.shutdownOutput();
        //outputStream.close();
        //bufferedWriter.close();
        //outputStreamWriter.close();

        InputStream in = clientSocket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String temp = null;
        StringBuffer sb = new StringBuffer();
        while ((temp = reader.readLine())!=null){
            sb.append(temp);
        }
        //System.out.println(sb);

        //clientSocket.shutdownInput();
        reader.close();
        inputStreamReader.close();
        //in.close();
        //outputStream.close();
        //clientSocket.close();

        return sb.toString();
    }

    public void close(){
        if (isClosed){
            return;
        }
        isClosed = true;
        try {
            this.clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

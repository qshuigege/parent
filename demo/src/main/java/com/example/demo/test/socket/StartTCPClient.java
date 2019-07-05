package com.example.demo.test.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class StartTCPClient {

    public static void main(String[] args) throws IOException {
        /*try {
            TCPSocketClient instance = TCPSocketClient.getInstance("localhost", 2233);
            while (true) {
                Scanner input = new Scanner(System.in);
                System.out.print("请输入要发送的信息：");
                String s = input.nextLine();
                if (s.equals("886")){
                    break;
                }
                String response = instance.send(s.getBytes());
                System.out.println(response);
            }
            instance.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/

        Socket socket = new Socket("localhost", 2233);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String str = bufferedReader.readLine();
            bufferedWriter.write(str);
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        }
    }

}





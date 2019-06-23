package com.example.demo.test.aio_test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;

public class AIO_Client {
    public static void main(String[] args) throws Exception{
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        clientChannel.connect(new InetSocketAddress("localhost", 8899), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                System.out.println("客户端成功连接到服务器！");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
        //向服务器发送消息

        while(true){
            //System.out.println("请输入要向服务器发送的内容：");
            Scanner input = new Scanner(System.in);
            String msg = input.nextLine();
            byte[] req = msg.getBytes();
            ByteBuffer writerBuffer = ByteBuffer.allocate(req.length);
            writerBuffer.put(req);
            writerBuffer.flip();
            clientChannel.write(writerBuffer, clientChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                @Override
                public void completed(Integer result, AsynchronousSocketChannel attachment) {
                    if (writerBuffer.hasRemaining()){
                        attachment.write(writerBuffer, attachment, this);
                    }else {

                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        attachment.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                attachment.flip();
                                byte[] bytes = new byte[attachment.remaining()];
                                attachment.get(bytes);
                                System.out.println("收到服务器的响应，响应内容为：“" + new String(bytes) + "”");
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {

                            }
                        });
                    }
                }

                @Override
                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                    System.out.println("发送数据失败！");
                }
            });


        }
    }
}

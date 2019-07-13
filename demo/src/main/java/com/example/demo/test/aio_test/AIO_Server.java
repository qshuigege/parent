package com.example.demo.test.aio_test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class AIO_Server {

    public static void main(String[] args) {
        AsynchronousServerSocketChannel channel = null;
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(8899));
            System.out.println("服务器已启动，端口号：8899");
            channel.accept(channel, new AcceptHandler());
            new CountDownLatch(1).await();//或者while (true){Thread.sleep(1000);}
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>{

    @Override
    public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
        attachment.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(2);
        result.read(buffer, result, new ReadHandler(buffer));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

    }
}

class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>{
    private ByteBuffer buffer;
    public ReadHandler(ByteBuffer b){
        this.buffer = b;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        buffer.flip();
        byte[] msg = new byte[buffer.remaining()];
        buffer.get(msg);
        System.out.println("服务器收到的消息："+new String(msg));

        String resp = "客户端你好，我已收到你的消息，消息内容为："+new String(msg);
        byte[] bytes = resp.getBytes();
        ByteBuffer wb = ByteBuffer.allocate(bytes.length);
        wb.put(bytes);
        wb.flip();
        attachment.write(wb, attachment, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel attachment) {
                if (wb.hasRemaining()){
                    attachment.write(wb, attachment, this);
                }else {
                    ByteBuffer readbf = ByteBuffer.allocate(2);
                    attachment.read(readbf, attachment, new ReadHandler(readbf));
                }
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

            }
        });
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

    }
}

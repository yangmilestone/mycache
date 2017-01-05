package com.yangwb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer
{
    public static final int PORT = 8888;//监听的端口号

    public static void main(String[] args)
    {
        System.out.println("服务器启动...\n");
        TestServer server = new TestServer();
        server.init();
    }

    public void init()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true)
            {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    private class HandlerThread implements Runnable
    {
        private Socket socket;

        public HandlerThread(Socket client)
        {
            socket = client;
            new Thread(this).start();
        }

        public void run()
        {
            try
            {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                // 处理客户端数据
                System.out.println("客户端发过来的内容:" + clientInputStr);

                // 向客户端回复信息
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String s = "我是服务器给你回信息了";
                out.writeUTF(s);

                out.close();
                input.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                if ( socket != null )
                {
                    try
                    {
                        socket.close();
                    } catch (Exception e)
                    {
                        socket = null;
                        System.out.println("服务端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
    }
}
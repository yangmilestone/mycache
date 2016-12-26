package com.yangwb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yangwb on 2016/12/26.
 */
public class StartMain
{
    public static void main(String[] args)
    {
        /**
         * todo bio方式实现内核，先实现c/s结构外壳，使其稳定正常通信
         * todo 其次，实现接受数据的业务逻辑，接受命令， 发送给redis
         */
        while (true)
        {
            pullData();
        }
    }

    private static void pullData()
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(3650);
            Socket connect = server.accept();
            OutputStream out = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write("hello,I am client!who are u?");
            writer.flush();
            connect.close();
            server.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

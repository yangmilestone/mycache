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
        //todo 研究  ServerSocket  Socket InputStream  OutputStream api
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

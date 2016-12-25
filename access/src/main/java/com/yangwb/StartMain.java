package com.yangwb;

import java.io.IOException;
import java.io.InputStream;
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
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(3650);
            Socket connect = server.accept();
            System.out.println("hahah");
            InputStream in = connect.getInputStream();
            System.out.println(in.read());
//            OutputStream out = connect.getOutputStream();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

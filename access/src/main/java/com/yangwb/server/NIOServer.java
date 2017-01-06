package com.yangwb.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 18:03
 */
public class NIOServer extends AccessServer
{
    public NIOServer(int port)
    {
        super(port);
    }

    @Override
    public void start()
    {
        try
        {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            ss.bind(new InetSocketAddress(port));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

package com.yangwb;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by yangwb  on 2016/12/26.
 */
public class ClientTest
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost",3650));
    }
}

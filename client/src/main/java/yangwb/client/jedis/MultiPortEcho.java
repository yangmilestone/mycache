package yangwb.client.jedis;// $Id$

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiPortEcho
{
    private int ports[];
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public MultiPortEcho(int ports[]) throws IOException
    {
        this.ports = ports;

        go();
    }

    private void go() throws IOException
    {
        // Create a new selector
        Selector selector = Selector.open();

        // Open a listener on each port, and register each one
        // with the selector
        for (int i = 0; i < ports.length; ++i)
        {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);

            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Going to listen on " + ports[i]);
        }

        while (true)
        {
            int num = selector.select();
            if ( num == 0 ) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();

            while (it.hasNext())
            {
                SelectionKey key = (SelectionKey) it.next();
                if ( key.isAcceptable() )
                {
                    System.out.println("lian jie ");
                    // Accept the new connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    // Add the new connection to the selector
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
                    it.remove();

                    System.out.println("Got connection from " + sc);
                }
                else if (key.isReadable())
                {
                    System.out.println("du qu ");
                    SocketChannel sc = (SocketChannel) key.channel();
                    int bytesEchoed = 0;
                    while (true)
                    {
                        echoBuffer.clear();
                        int r = sc.read(echoBuffer);
                        if ( r <= 0 )
                        {
                            break;
                        }
                        echoBuffer.flip();
                        sc.write(echoBuffer);
                        bytesEchoed += r;
                    }
                    System.out.println("Echoed " + bytesEchoed + " from " + sc);
                    sc.register(selector, SelectionKey.OP_WRITE);
                    it.remove();
                }
                else if ( key.isWritable() )
                {
                    System.out.println("xie ru ");
                    it.remove();
                    break;
                }

            }

            //System.out.println( "going to clear" );
            //      selectedKeys.clear();
            //System.out.println( "cleared" );
        }
    }

    static public void main(String args[]) throws Exception
    {


        int ports[] = new int[]{36350};

        new MultiPortEcho(ports);
    }
}

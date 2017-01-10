package com.yangwb.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 18:03
 */
public class NIOServer extends AccessServer
{
    private static Logger logger = Logger.getLogger(NIOServer.class);

    public NIOServer(int port)
    {
        super(port);
    }

    @Override
    public void start()
    {
        try
        {
            Selector selector = Selector.open();
            logger.debug("NIO服务器已经启动 端口号:" + port);
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            ss.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.debug("注册好监听连接事件了！");
            while (true)
            {
                int n = selector.select();
                if ( n < 1 )
                {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext())
                {
                    SelectionKey key = iterator.next();
                    System.out.println(key);
                    if ( key.isAcceptable() )
                    {
                        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel channel = socketChannel.accept();
                        // 设为非阻塞模式
                        channel.configureBlocking(false);
                        // 注册该channel到选择器上
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        key.interestOps(SelectionKey.OP_ACCEPT);
                        buffer.clear();
                        if ( channel.read(buffer) < 0 )
                        {
                            channel.close();
                        }
                        else
                        {
                            buffer.flip();
                            String receiveMsg = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
                            System.out.println("receive client message: " + receiveMsg + " from " + channel.getRemoteAddress());
                        }
                    }
                    iterator.remove();
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

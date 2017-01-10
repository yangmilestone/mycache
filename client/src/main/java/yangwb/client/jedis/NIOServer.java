package yangwb.client.jedis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author: rhwayfun
 * @since: 2016-05-28
 */
public class NIOServer
{
    // 服务器监听的端口
    private static final int PORT = 3651;
    // 处理数据的缓冲区
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    // 欢迎消息
    private static final String GREETING = "Welcome to here.";

    public static void main(String[] args)
    {
        new NIOServer().start();
    }

    private void start()
    {
        int port = PORT;
        System.out.println("listening on port " + port);
        Iterator<SelectionKey> iterator = null;
        try
        {
            //创建一个ServerChannel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            //获取通道关联的Socket对象
            ServerSocket serverSocket = serverChannel.socket();
            //要绑定的地址
            SocketAddress address = new InetSocketAddress(port);
            //创建需要注册的选择器
            Selector selector = Selector.open();

            //把socket对象绑定到指定的地址
            serverSocket.bind(address);

            //配置为非阻塞模式
            serverChannel.configureBlocking(false);

            //注册通道到选择器
            //第二个参数表名serverChannel感兴趣的事件是OP_ACCEPT类型的事件
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 选择器不断循环从选择器中选取已经准备好的通道进行操作
            // 选取之后，会对其感兴趣的事件进行处理。将感兴趣的事件
            // 处理完毕后将key从集合中删除，表示该通道的事件已经处
            // 理完毕

            while (true)
            {
                // 这个操作可能会被阻塞，因为不知道注册在这个选择器上的通道是否准备好了
                int n = selector.select();
                if ( n == 0 )
                {
                    continue;
                }

                // 获取SelectionKey的迭代器对象
                iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext())
                {
                    // 获取这个key关联的通道
                    SelectionKey key = iterator.next();
                    // 判断感兴趣的事件类型
                    if ( key.isAcceptable() )
                    {
                        // 这里可以强制转换为ServerSocketChannel
                        // 因为在这个选择器上目前只注册了一个该类型的通道
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        // 调用accept方法可以得到连接到此地址的客户端连接
                        SocketChannel channel = server.accept();
                        // 注册客户端连接到选择器上，并把感兴趣的事件类型设为可读类型
                        registerChannel(selector, channel, SelectionKey.OP_READ);
                        // 给客户端发送响应消息
//                        sayHello(channel);
                    }

                    // 如果是可读类型的事件，则获取传输过来的数据
                    if ( key.isReadable() )
                    {
                        readDataFromClient(key);
                    }

                    // 将已经处理的key从集合中删除
                    iterator.remove();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            iterator.remove();
        }

    }

    /**
     * @param key
     */
    private void readDataFromClient(SelectionKey key) throws IOException
    {
        // 获取key管理的Channel对象
        SocketChannel channel = (SocketChannel) key.channel();
        // 读取之前需要清空缓冲区
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

    /**
     * 向客户端发送响应消息
     *
     * @param channel
     * @throws IOException
     */
    private void sayHello(SocketChannel channel) throws IOException
    {
        buffer.clear();
        buffer.put(GREETING.getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    /**
     * 注册客户端连接到选择器上
     *
     * @param selector
     * @param channel
     * @param opRead
     * @throws IOException
     */
    private void registerChannel(Selector selector, SocketChannel channel, int opRead) throws IOException
    {
        if ( channel == null )
        {
            return;
        }
        // 设为非阻塞模式
        channel.configureBlocking(false);
        // 注册该channel到选择器上
        channel.register(selector, opRead);
    }

}

class SelectorThread extends Thread
{

    private Selector selector;

    public SelectorThread(Selector selector)
    {
        this.selector = selector;
    }

    @Override
    public void run()
    {
        try
        {
            // 获取Selector注册的通道数
            int n = selector.select();
            while (n > 0)
            {
                // selector.selectedKeys()可以获取每个注册通道的key
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext())
                {
                    SelectionKey key = iterator.next();
                    if ( key.isReadable() )
                    {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        buffer.flip();
                        String receiveMsg = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
                        System.out.println("receive server message: " + receiveMsg + " from " + channel.getRemoteAddress());
                        key.interestOps(SelectionKey.OP_READ);
                    }
                    // 处理下一个事件
                    iterator.remove();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
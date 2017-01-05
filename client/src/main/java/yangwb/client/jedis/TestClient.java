package yangwb.client.jedis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestClient
{
    public static final String IP_ADDR = "localhost";//服务器地址
    public static final int PORT = 8888;//服务器端口号

    public static void main(String[] args)
    {
        System.out.println("客户端启动...");
        Socket socket = null;
        try
        {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(IP_ADDR, PORT);

            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String str = "我是客户端";
            out.writeUTF(str);

            String ret = input.readUTF();
            System.out.println("服务器端返回过来的是: " + ret);
            out.close();
            input.close();
        } catch (Exception e)
        {
            System.out.println("客户端异常:" + e.getMessage());
        } finally
        {
            if ( socket != null )
            {
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
    }
}
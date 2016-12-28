package com.yangwb;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by yangwb  on 2016/12/26.
 */
public class ClientTest
{
    public static void main(String[] args) throws IOException
    {
        //        other();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                other();
            }
        }).start();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                other();
            }
        }).start();

    }

    private static void test()
    {
        try
        {
            Socket s = new Socket("127.0.0.1", 8888);
            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器：" + mess);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void other()
    {
        Socket socket = null;
        try
        {
            socket = new Socket("127.0.0.1", 3650);
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            for (int i = reader.read(); i != -1; i = reader.read())
            {
                System.out.print((char) i);
            }
            System.out.println("hahahh");
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if ( socket != null )
                {
                    socket.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

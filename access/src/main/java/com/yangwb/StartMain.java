package com.yangwb;


import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangwb on 2016/12/26.
 */
public class StartMain
{
    private static Logger logger = Logger.getLogger(StartMain.class);

    public static void main(String[] args)
    {

        ExecutorService pool = Executors.newFixedThreadPool(50);
        /**
         * todo bio方式实现内核，先实现c/s结构外壳，使其稳定正常通信
         * todo 其次，实现接受数据的业务逻辑，接受命令， 发送给redis
         */
        try (ServerSocket server = new ServerSocket(3650)) //todo 放入配置文件中去
        {
            logger.debug("服务器已经启动！");
            while (true)
            {
                Socket connect = server.accept();
                pool.submit(new SendDataThread(connect));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static class SendDataThread extends Thread
    {
        private Socket connect;

        SendDataThread(Socket connect)
        {
            this.connect = connect;
        }
        @Override
        public void run()
        {
            try
            {
                logger.debug("准备接受连接");
                OutputStream out = connect.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write("hello,I am server!who are u?");
                writer.flush();
                logger.debug("已经发送数据");
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    connect.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

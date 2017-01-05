package com.yangwb.server;

import com.util.ConfigUtil;
import com.util.KeyHash;
import org.apache.log4j.Logger;
import redis.clients.jedis.Protocol;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 18:03
 */
public class BIOServer extends AccessServer
{
    private static Logger logger = Logger.getLogger(BIOServer.class);

    @Override
    public void start()
    {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        /**
         * todo bio方式实现内核，先实现c/s结构外壳，使其稳定正常通信
         * todo 其次，实现接受数据的业务逻辑，接受命令， 发送给redis
         */
        try (ServerSocket server = new ServerSocket(3650))
        {
            logger.debug("服务器已经启动！");
            while (true)
            {
                Socket connect = server.accept();
                System.out.println("接受连接了！");
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

                System.out.println("进run方法了");
                while (true)
                {
                    InputStream is = connect.getInputStream();
                    RedisInputStream ris = new RedisInputStream(is);
                    RedisOutputStream ros = new RedisOutputStream(connect.getOutputStream());
                    try
                    {
                        Object obj = Protocol.read(ris);
                        if ( obj instanceof byte[] )
                        {
                            String recv = new String((byte[]) obj);
                        }
                        else if ( obj instanceof Long )
                        {
                            Long recv = (Long) obj;
                        }
                        else if ( obj instanceof List )
                        {
                            List<String> cmd = new ArrayList<>();
                            List list = (List) obj;
                            for (Object o : list)
                            {
                                cmd.add(new String((byte[]) o));
                            }
                            if ( cmd.get(0).equals("SET") )
                            {
                                String key = cmd.get(1);
                                int slot = KeyHash.keyHashSlot(key.getBytes(), key.length());
                                int port = ConfigUtil.getPortBySlot(slot);
//                                String host = getHost(slot);
//                                Jedis jedis = new Jedis()
                            }
                        } ros.writeUtf8CrLf("+ok");
                        ros.flush();
                    } catch (Exception e)
                    {
                        break;
                    }

                    logger.debug("已经发送数据");
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    connect.close();
                    logger.debug("服务器连接关闭");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}

package com.yangwb;


import com.util.Contans;
import com.yangwb.server.AccessServer;
import com.yangwb.server.BIOServer;
import com.yangwb.server.NIOServer;
import org.apache.log4j.Logger;

/**
 * Created by yangwb on 2016/12/26.
 */
public class StartMain
{
    private static Logger logger = Logger.getLogger(StartMain.class);
    private static Contans.Model type = Contans.Model.BIO;

    public static void main(String[] args)
    {
        AccessServer server = getServer();
        server.start();
    }

    private static AccessServer getServer()
    {
        if ( type.equals(Contans.Model.BIO) )
        {
            return new BIOServer();
        }
        else
        {
            return new NIOServer();
        }
    }
}

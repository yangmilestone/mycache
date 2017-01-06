package com.yangwb.server;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 16:52
 */
public abstract class AccessServer
{
    protected int port;
    public AccessServer(int port)
    {
        this.port = port;
    }

    public abstract void start();
}

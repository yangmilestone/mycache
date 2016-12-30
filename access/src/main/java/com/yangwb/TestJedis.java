package com.yangwb;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2016/12/30
 * Time: 17:34
 */
public class TestJedis
{
    public static void main(String[] args)
    {
        Jedis jedis = new Jedis("192.168.40.130", 56501);
        System.out.println(jedis.set("hehe", "hehe"));
        //todo 下载jedis源码

    }

}

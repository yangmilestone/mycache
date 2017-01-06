package com.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 16:53
 */
public final class Contans
{
    public static enum Model
    {
        NIO, BIO;
    }
    public static Map<Integer, Integer[]> slotMapping = new HashMap<>();
    public static Map<Integer, String > redisUrlMapping = new HashMap<>();

    static
    {
        slotMapping.put(56503, new Integer[]{0,8192});
        slotMapping.put(56501, new Integer[]{8193,16384});
        redisUrlMapping.put(56501, "192.168.40.129");
        redisUrlMapping.put(56503, "192.168.40.129");
    }

    public final static int SERVER_PORT = 3650;
    public final static int POOL_SIZE = 50;
}

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

    public static Map<Integer, Map<String, Integer>> slotMapping = new HashMap<>();
    public static Map<Integer, String > redisUrlMapping = new HashMap<>();

    static
    {
        Map<String, Integer> redis503 = new HashMap<>();
        redis503.put("start", 0);
        redis503.put("end", 8192);
        slotMapping.put(56503, redis503);
        Map<String, Integer> redis501 = new HashMap<>();
        redis501.put("start", 8193);
        redis501.put("end", 16384);
        slotMapping.put(56501, redis501);
        redisUrlMapping.put(56501, "192.168.40.128");
        redisUrlMapping.put(56503, "192.168.40.128");
    }
}

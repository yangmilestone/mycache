package com.util;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yangwb
 * Date: 2017/1/5
 * Time: 18:24
 */
public class ConfigUtil
{

    public static int getPortBySlot(int slot)
    {
        Map<Integer,Integer[]> map = Contans.slotMapping;
        for (Integer port : map.keySet())
        {
            if ( Math.max(slot, map.get(port)[0]) == Math.min(slot, map.get(port)[1]) )
            {
                return port;
            }

        }
        throw new RuntimeException("找不到对应槽段的端口");
    }

    public static String getHostByPort(int port)
    {
        return Contans.redisUrlMapping.get(port);
    }
}

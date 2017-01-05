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
        Map<Integer,Map<String,Integer>> map = Contans.slotMapping;
        for (Integer integer : map.keySet())
        {
            int port = integer;
            for (String s : map.get(integer).keySet())
            {
                //// TODO: 2017/1/5  
            }
        }
        return 0;
    }
}

package com.xloya.wenda.utils;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";

    public static String getLikeKey(int entity_type,int entity_id){
        return BIZ_LIKE+SPLIT+String.valueOf(entity_type)+SPLIT+String.valueOf(entity_id);
    }


    public static String getDislikeKey(int entity_type,int entity_id){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entity_type)+SPLIT+String.valueOf(entity_id);
    }

    public static String getBizEventqueue(){
        return BIZ_EVENTQUEUE;
    }
}

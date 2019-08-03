package com.xloya.wenda.service;

import com.xloya.wenda.utils.JedisAdapter;
import com.xloya.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    public long getLikeCount(int entity_type,int entity_id){
        String likeKey = RedisKeyUtil.getLikeKey(entity_type, entity_id);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int user_id,int entity_type,int entity_id){
        String likeKey = RedisKeyUtil.getLikeKey(entity_type, entity_id);
        if(jedisAdapter.sismember(likeKey,String.valueOf(user_id))){
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entity_type, entity_id);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(user_id)) ? -1 : 0;
    }
    public long like(int user_id,int entity_type,int entity_id){
        String likeKey = RedisKeyUtil.getLikeKey(entity_type, entity_id);
        jedisAdapter.sadd(likeKey,String.valueOf(user_id));

        String dislikeKey = RedisKeyUtil.getDislikeKey(entity_type, entity_id);
        jedisAdapter.srem(dislikeKey,String.valueOf(user_id));

        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int user_id,int entity_type,int entity_id){
        String dislikeKey = RedisKeyUtil.getLikeKey(entity_type, entity_id);
        jedisAdapter.sadd(dislikeKey,String.valueOf(user_id));

        String likeKey = RedisKeyUtil.getDislikeKey(entity_type, entity_id);
        jedisAdapter.srem(likeKey,String.valueOf(user_id));

        return jedisAdapter.scard(likeKey);
    }
}

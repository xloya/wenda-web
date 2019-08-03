package com.xloya.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.xloya.wenda.utils.JedisAdapter;
import com.xloya.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getBizEventqueue();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}

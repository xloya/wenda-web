package com.xloya.wenda.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            LOGGER.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            LOGGER.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            LOGGER.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            LOGGER.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;
    }
}

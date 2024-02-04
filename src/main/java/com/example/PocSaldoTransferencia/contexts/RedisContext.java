package com.example.PocSaldoTransferencia.contexts;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisContext {
    
    private JedisPool jedisPool;

    public Jedis getConnection(){
        jedisPool = new JedisPool("redis", 6379, "default", "admin");
        return jedisPool.getResource();
    }


    public void close(){
        jedisPool.close();
    }
}

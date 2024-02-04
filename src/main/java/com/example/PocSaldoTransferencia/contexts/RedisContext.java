package com.example.PocSaldoTransferencia.contexts;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisContext {
    
    private JedisPool jedisPool;

    public Jedis getConnection(){
        jedisPool = new JedisPool("localhost", 6379, "redis", "admin");
        return jedisPool.getResource();
    }


    public void close(){
        jedisPool.close();
    }
}

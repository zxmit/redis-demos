package com.zxm.redis.redisson.practice.cache;

import com.kingcobra.rredis.RedisConnector;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.concurrent.TimeUnit;

/**
 * Created by zxm on 8/25/16.
 */
public class MapCacheOperator {

    private RedisConnector connector;
    private RedissonClient redisson;

    public MapCacheOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void add(String key, long ttl) {
        RMapCache<String, String> map = redisson.getMapCache(key, new StringCodec());
        map.put("key_1", "value_1", ttl, TimeUnit.SECONDS);
    }

    public String get(String key) {
        RMapCache<String, String> map = redisson.getMapCache(key, new StringCodec());
        return map.get("key_1");
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) throws InterruptedException {
        MapCacheOperator operator = new MapCacheOperator();

        operator.add("mydb:mytab1:col10", 1);
        System.out.println("values: " + operator.get("mydb:mytab1:col10"));

        Thread.sleep(1000);
        System.out.println("values: " + operator.get("mydb:mytab1:col10"));

        operator.delKeys("mydb:mytab1:col10");
        operator.close();
    }
}

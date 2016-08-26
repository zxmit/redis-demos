package com.zxm.redis.redisson.practice.cache;

import com.kingcobra.rredis.RedisConnector;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxm on 8/25/16.
 */
public class SetCacheOperator {

    private RedisConnector connector;
    private RedissonClient redisson;

    public SetCacheOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void add(String key, long ttl) {
        RSetCache<String> set = redisson.getSetCache(key, new StringCodec());
        set.add("value_1", ttl, TimeUnit.SECONDS);
    }

    public Set<String> get(String key) {
        RSetCache<String> set = redisson.getSetCache(key, new StringCodec());
        return set.size() == 0? null:set.readAll();
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) throws InterruptedException {
        SetCacheOperator operator = new SetCacheOperator();

        operator.add("mydb:mytab1:col9", 1);
        Set<String> set = operator.get("mydb:mytab1:col9");
        if(set != null) {
            System.out.println("values: " + set.toString());
        }

        Thread.sleep(1000);

        set = operator.get("mydb:mytab1:col9");
        if(set != null) {
            System.out.println("values: " + set.toString());
        }
        operator.delKeys("mydb:mytab1:col9");
        operator.close();
    }
}

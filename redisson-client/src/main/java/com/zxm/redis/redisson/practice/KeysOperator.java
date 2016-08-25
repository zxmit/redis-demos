package com.zxm.redis.redisson.practice;

import com.zxm.redis.redisson.utils.ClientCreator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;

import java.util.*;

/**
 * Created by zxm on 8/25/16.
 */
public class KeysOperator {

    private RedissonClient redisson;

    public KeysOperator() {
        redisson = ClientCreator.createInstance();
    }

    public void setSync(String key, String value) {
        redisson.getBucket(key).set(value);
    }

    public void setAsync(String key, String value) {
        RFuture<Void> future = redisson.getBucket(key).setAsync(value);
        future.addListener(new FutureListener<Void>() {
            public void operationComplete(Future<Void> voidFuture) throws Exception {
                if(voidFuture.isSuccess()) {
                    System.out.println("put value success");
                } else {
                    System.out.println("put value failed");
                }
            }
        });
    }

    public String get(String key) {
        RBucket<String> bucket = redisson.getBucket(key);
        return bucket == null ? null:bucket.get().toString();
    }

    public Map<String,String> getByPattern(String key_pattern) {
        Map<String,String> maps = new HashMap<String, String>();
        Iterator<String> iterator = redisson.getKeys().getKeysByPattern(key_pattern).iterator();
        for (; iterator.hasNext();) {
            String key = iterator.next();
            maps.put(key, get(key));
        }
        return maps;
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public long delKeysByPattern(String key_pattern) {
        return redisson.getKeys().deleteByPattern(key_pattern);
    }

    public void close() {
        redisson.shutdown();
    }

    public static void main(String[] args) {
        KeysOperator operator = new KeysOperator();

        // 增添 or 修改
        operator.setSync("mydb:mytab1:col2", "test_value_1");
        operator.setSync("mydb:mytab1:col2", "test_value_2");
        operator.setAsync("mydb:mytab2:col1", "test_value_1");
        operator.setAsync("mydb:mytab2:col2", "test_value_2");

        // 查询
        System.out.println(operator.get("mydb:mytab1:col2"));
        System.out.println(operator.getByPattern("mydb:mytab1:col?"));

        // 删除
        System.out.println(operator.delKeys("mydb:mytab1:col1", "mydb:mytab1:col2"));
        System.out.println(operator.delKeysByPattern("mydb:mytab2:col?"));

        operator.close();
    }


}

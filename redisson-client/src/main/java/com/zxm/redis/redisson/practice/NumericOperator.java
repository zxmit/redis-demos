package com.zxm.redis.redisson.practice;

import com.kingcobra.rredis.RedisConnector;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;

/**
 * Created by zxm on 8/25/16.
 * 本示例中以Long类型作为演示,Double类型于此类似
 */
public class NumericOperator {

    private RedisConnector connector;
    private RedissonClient redisson;

    public NumericOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void setSync(String key, long value) {
        redisson.getAtomicLong(key).set(value);
    }

    public void setAsync(String key, long value) {
        RFuture<Void> future = redisson.getAtomicLong(key).setAsync(value);
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

    public long get(String key) {
        RAtomicLong value = redisson.getAtomicLong(key);
        return value == null ? null:value.get();
    }

    /**
     * 先增加,后返回值
     * @param key
     * @param delta
     * @return
     */
    public long incrementAndGet(String key, long delta) {
        RAtomicLong value = redisson.getAtomicLong(key);
        return value == null? null:value.addAndGet(delta);
    }

    /**
     * 先返回值,后增加
     * @param key
     * @param delta
     * @return
     */
    public long getAndIncrement(String key, long delta) {
        RAtomicLong value = redisson.getAtomicLong(key);
        return value == null? null:value.getAndAdd(delta);
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public long delKeysByPattern(String key_pattern) {
        return redisson.getKeys().deleteByPattern(key_pattern);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) {
        NumericOperator operator = new NumericOperator();

        // 增添 or 修改
        operator.setSync("mydb:mytab1:col3", 10);
        operator.setAsync("mydb:mytab2:col3", 10);

        // 查询
        System.out.println(operator.get("mydb:mytab1:col3"));

        // 修改
        System.out.println(operator.getAndIncrement("mydb:mytab1:col3", 5));
        System.out.println(operator.incrementAndGet("mydb:mytab2:col3", 5));

        // 删除
        System.out.println(operator.delKeys("mydb:mytab1:col3", "mydb:mytab1:test2"));
        System.out.println(operator.delKeysByPattern("mydb:mytab2:*"));

        operator.close();
    }
}

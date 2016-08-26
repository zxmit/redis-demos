package com.zxm.redis.redisson.practice;

import com.kingcobra.rredis.RedisConnector;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Set;

/**
 * Created by zxm on 8/25/16.
 */
public class SetOperator {

    private RedissonClient redisson;
    private RedisConnector connector;

    public SetOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void addSync(String key, String value) {
        RSet<String> set = redisson.getSet(key);
        set.add(value);
    }

    public void addAsync(String key, String value) {
        RFuture<Boolean> future = redisson.getSet(key).addAsync(value);
        future.addListener(new FutureListener<Boolean>() {
            public void operationComplete(Future<Boolean> voidFuture) throws Exception {
                if(voidFuture.isSuccess()) {
                    System.out.println("put value success");
                } else {
                    System.out.println("put value failed");
                }
            }
        });
    }

    public Set<String> getAllValue(String key) {
        RSet<String> set = redisson.getSet(key);
        return set == null? null : set.readAll();
    }

    public String getRandomValue(String key) {
        RSet<String> set = redisson.getSet(key);
        return set.random();
    }

    public void remove(String key, String value) {
        RSet<String> set = redisson.getSet(key);
        set.remove(value);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) {
        SetOperator operator = new SetOperator();

        // 增添
        operator.addSync("mydb:mytab1:col6", "value_5");
        operator.addAsync("mydb:mytab1:col6", "value_6");
        operator.addAsync("mydb:mytab1:col6", "value_7");
        operator.addAsync("mydb:mytab1:col6", "value_8");

        // 查询
        System.out.println(operator.getRandomValue("mydb:mytab1:col6"));
        System.out.println(operator.getAllValue("mydb:mytab1:col6"));

        // 删除
        operator.remove("mydb:mytab1:col6", "value_5");
        System.out.println(operator.getAllValue("mydb:mytab1:col6"));

        operator.close();
    }
}

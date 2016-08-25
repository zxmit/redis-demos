package com.zxm.redis.redisson.practice;

import com.zxm.redis.redisson.utils.ClientCreator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by zxm on 8/25/16.
 */
public class SorttedSetOperator {

    private RedissonClient redisson;

    public SorttedSetOperator() {
        redisson = ClientCreator.createInstance();
    }

    public void addSync(String key, int value) {
        RSortedSet<Integer> set = redisson.getSortedSet(key);
        set.add(value);
    }

    public void addAsync(String key, int value) throws ExecutionException, InterruptedException {
        RFuture<Boolean> future = redisson.getSortedSet(key).addAsync(value);
        future.addListener(new FutureListener<Boolean>() {
            public void operationComplete(Future<Boolean> voidFuture) throws Exception {
                if(voidFuture.isSuccess()) {
                    System.out.println("put value success");
                } else {
                    voidFuture.cause().printStackTrace();
                }
            }
        });
    }

    public int getFirstValue(String key) {
        RSortedSet<Integer> set = redisson.getSortedSet(key);
        return set == null? null : set.first();
    }

    public List<Integer> getAllVlaue(String key) {
        RSortedSet<Integer> set = redisson.getSortedSet(key);
        if(set == null) return null;
        List<Integer> result = new ArrayList<Integer>();
        for(Integer i : set) {
            result.add(i);
        }
        return result;
    }

    public boolean removeValue(String key, int value) {
        RSortedSet<Integer> set = redisson.getSortedSet(key);
        return set == null? null : set.remove(value);
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        redisson.shutdown();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SorttedSetOperator operator = new SorttedSetOperator();

        // 增添
        operator.addSync("mydb:mytab1:col7", 2);
        operator.addSync("mydb:mytab1:col7", 5);
        operator.addSync("mydb:mytab1:col7", 3);

        // 异步添加数据会出现异常,不赞成使用,原因未知
//        operator.addAsync("mydb:mytab1:col7", 6);
//        operator.addAsync("mydb:mytab1:col7", 4);
//        operator.addAsync("mydb:mytab1:col7", 7);

        // 查询
        System.out.println(operator.getFirstValue("mydb:mytab1:col7"));
        System.out.println(operator.getAllVlaue("mydb:mytab1:col7"));

        // 删除
        operator.removeValue("mydb:mytab1:col7", 3);
        System.out.println(operator.getAllVlaue("mydb:mytab1:col7"));

        operator.delKeys("mydb:mytab1:col7");

        operator.close();
    }

}

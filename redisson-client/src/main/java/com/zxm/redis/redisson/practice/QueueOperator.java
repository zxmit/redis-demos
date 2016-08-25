package com.zxm.redis.redisson.practice;

import com.zxm.redis.redisson.utils.ClientCreator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * Created by zxm on 8/25/16.
 */
public class QueueOperator {

    private RedissonClient redisson;

    public QueueOperator() {
        redisson = ClientCreator.createInstance();
    }

    public void addSync(String key, String value) {
        RQueue<String> queue = redisson.getQueue(key);
        queue.add(value);
    }

    public void addAsync(String key, String value) {
        RFuture<Boolean> future = redisson.getQueue(key).addAsync(value);
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

    public String element(String key) {
        RQueue<String> queue = redisson.getQueue(key);
        return queue.size() == 0? null:queue.element();
    }

    public void remove(String key) {
        RQueue<String> queue = redisson.getQueue(key);
        queue.remove();
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        redisson.shutdown();
    }

    public static void main(String[] args) {
        QueueOperator operator = new QueueOperator();

        // 增添
//        operator.addSync("mydb:mytab1:col8", "value_5");
//        operator.addAsync("mydb:mytab1:col8", "value_6");
//        operator.addAsync("mydb:mytab1:col8", "value_7");
//        operator.addAsync("mydb:mytab1:col8", "value_8");

        // 查询
        System.out.println(operator.element("mydb:mytab1:col8"));

        // 删除
        operator.remove("mydb:mytab1:col8");
        System.out.println(operator.element("mydb:mytab1:col8"));

        operator.delKeys("mydb:mytab1:col8");

        operator.close();
    }
}

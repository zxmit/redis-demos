package com.zxm.redis.redisson.practice;

import com.kingcobra.rredis.RedisConnector;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxm on 8/25/16.
 */
public class ListOperator {

    private RedisConnector connector;
    private RedissonClient redisson;

    public ListOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void addSync(String key, String value) {
        RList list = redisson.getList(key);
        list.add(value);
    }

    public void addAsync(String key, String value) {
        RFuture<Boolean> future = redisson.getList(key).addAsync(value);
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

    public String getValue(String key, int index) {
        RList<String> list = redisson.getList(key);
        if(index >= list.size()) {
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+list.size());
        }
        return list.get(index);
    }

    public List<String> getAllValues(String key) {
        RList<String> list = redisson.getList(key);
        return list.readAll();
    }

    public List<String> trimValues(String key, int from, int end) {
        RList<String> list = redisson.getList(key);
        if(list == null) {
            return null;
        }
        list.trim(from, end);
        return list.readAll();
    }

    public void removeValue(String key, int index) {
        RList<String> list = redisson.getList(key);
        if(index >= list.size()) {
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+list.size());
        }
        list.remove(index);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) {
        ListOperator operator = new ListOperator();

        // 增添
        operator.addSync("mydb:mytab1:col5", "value_5");
        operator.addAsync("mydb:mytab1:col5", "value_6");

        // 查询
        System.out.println(operator.getValue("mydb:mytab1:col5", 0));
        System.out.println(operator.getAllValues("mydb:mytab1:col5"));

        // 删除
        operator.removeValue("mydb:mytab1:col5", 0);
        System.out.println(operator.getAllValues("mydb:mytab1:col5"));

        operator.close();
    }

}

package com.zxm.redis.redisson.practice;

import com.zxm.redis.redisson.utils.ClientCreator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxm on 8/25/16.
 */
public class HashOperators {

    private RedissonClient redisson;

    public HashOperators() {
        redisson = ClientCreator.createInstance();
    }

    public void putSync(String key, Map<String, String> values) {
        RMap<String, String> map = redisson.getMap(key);
        map.putAll(values);
//        map.put("somekey", "somevalue");
    }

    public void putAsync(String key, Map<String, String> values) {
        RMap<String, String> map = redisson.getMap(key);
        RFuture<Void> future = map.putAllAsync(values);
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

    public String getValue(String key, String mkey) {
        Map<String, String> map = redisson.getMap(key);
        return map == null? null:map.get(mkey);
    }

    public void updateValue(String key, String mkey, String newValue) {
        RMap<String, String> map = redisson.getMap(key);
        map.replace(mkey, newValue);
    }

    public void removeValue(String key, String mkey) {
        RMap<String, String> map = redisson.getMap(key);
        map.remove(mkey);
    }

    public void close() {
        redisson.shutdown();
    }

    public static void main(String[] args) {
        HashOperators operator = new HashOperators();

        Map<String, String> map = new HashMap<String, String>();
        map.put("key_1", "value_1");
        map.put("key_2", "value_2");
        map.put("key_3", "value_3");
        map.put("key_4", "value_4");

        // 增添
        operator.putSync("mydb:mytab1:col4", map);

        // 查询
        System.out.println(operator.getValue("mydb:mytab1:col4", "key_3"));

        //修改
        operator.updateValue("mydb:mytab1:col4", "key_3", "new_value_3");
        System.out.println(operator.getValue("mydb:mytab1:col4", "key_3"));

        // 删除
        operator.removeValue("mydb:mytab1:col4", "key_4");
        System.out.println(operator.getValue("mydb:mytab1:col4", "key_4"));
        operator.close();
    }
}

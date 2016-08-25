package com.zxm.redis.redisson.practice.multipmap;

import com.zxm.redis.redisson.practice.QueueOperator;
import com.zxm.redis.redisson.utils.ClientCreator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RFuture;
import org.redisson.api.RListMultimap;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.List;

/**
 * Created by zxm on 8/25/16.
 */
public class ListMultimapOperator {

    private RedissonClient redisson;

    public ListMultimapOperator() {
        redisson = ClientCreator.createInstance();
    }

    public void add(String key) {
        RListMultimap<String, String> map = redisson.getListMultimap(key, new StringCodec());
        map.put("key_1", "value_1_1");
        map.put("key_1", "value_1_2");
        map.put("key_1", "value_1_3");
        map.put("key_2", "value_1_1");
        map.put("key_2", "value_1_2");
        map.put("key_2", "value_1_3");

    }


    public List<String> get(String key, String mkey) {
        RListMultimap<String, String> map = redisson.getListMultimap(key, new StringCodec());
        return map.size()==0? null:map.get(mkey);
    }

    public void removeMultiKey(String key, String mkey) {
        RListMultimap<String, String> map = redisson.getListMultimap(key, new StringCodec());
        if(map.size() == 0) return;
        map.removeAll(mkey);
    }

    public void removeKeyValue(String key, String mkey, String mvalue) {
        RListMultimap<String, String> map = redisson.getListMultimap(key, new StringCodec());
        if(map.size() == 0) return;
        map.remove(mkey, mvalue);
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        redisson.shutdown();
    }

    public static void main(String[] args) {
        ListMultimapOperator operator = new ListMultimapOperator();

        // 增添
        operator.add("mydb:mytab1:col11");

        // 查询
        System.out.println(operator.get("mydb:mytab1:col11", "key_1"));

        // 删除
        operator.removeKeyValue("mydb:mytab1:col11", "key_1", "value_1_2");
        System.out.println(operator.get("mydb:mytab1:col11", "key_1"));

        operator.removeMultiKey("mydb:mytab1:col11", "key_1");
        System.out.println(operator.get("mydb:mytab1:col11", "key_1"));

        operator.delKeys("mydb:mytab1:col11");

        operator.close();
    }
}

package com.zxm.redis.redisson.practice.multipmap;

import com.kingcobra.rredis.RedisConnector;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.Set;

/**
 * Created by zxm on 8/25/16.
 */
public class SetMultimapOperator {

    private RedisConnector connector;
    private RedissonClient redisson;

    public SetMultimapOperator() {
        connector = RedisConnector.getInstance();
        redisson = connector.getRedisClient();
    }

    public void add(String key) {
        RSetMultimap<String, String> map = redisson.getSetMultimap(key, new StringCodec());
        map.put("key_1", "value_1_1");
        map.put("key_1", "value_1_2");
        map.put("key_1", "value_1_3");
        map.put("key_2", "value_1_1");
        map.put("key_2", "value_1_2");
        map.put("key_2", "value_1_3");

    }


    public Set<String> get(String key, String mkey) {
        RSetMultimap<String, String> map = redisson.getSetMultimap(key, new StringCodec());
        return map.size()==0? null:map.get(mkey);
    }

    public void removeMultiKey(String key, String mkey) {
        RSetMultimap<String, String> map = redisson.getSetMultimap(key, new StringCodec());
        if(map.size() == 0) return;
        map.removeAll(mkey);
    }

    public void removeKeyValue(String key, String mkey, String mvalue) {
        RSetMultimap<String, String> map = redisson.getSetMultimap(key, new StringCodec());
        if(map.size() == 0) return;
        map.remove(mkey, mvalue);
    }

    public long delKeys(String... keys) {
        return redisson.getKeys().delete(keys);
    }

    public void close() {
        connector.closeClient(redisson);
    }

    public static void main(String[] args) {
        SetMultimapOperator operator = new SetMultimapOperator();

        // 增添
        operator.add("mydb:mytab1:col12");

        // 查询
        System.out.println(operator.get("mydb:mytab1:col12", "key_1"));

        // 删除
        operator.removeKeyValue("mydb:mytab1:col12", "key_1", "value_1_2");
        System.out.println(operator.get("mydb:mytab1:col12", "key_1"));

        operator.removeMultiKey("mydb:mytab1:col12", "key_1");
        System.out.println(operator.get("mydb:mytab1:col12", "key_1"));

        operator.delKeys("mydb:mytab1:col12");

        operator.close();
    }
}

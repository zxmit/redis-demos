package com.zxm.redis.redisson.practice;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

/**
 * Created by zxm on 8/24/16.
 */
public class RedissonClient01 {

    public static void main(String[] args) {
        Config config = new Config();
        config.setUseLinuxNativeEpoll(false);
        config.useClusterServers()
                .addNodeAddress("node1:7005");
        RedissonClient redisson = Redisson.create(config);
        RBucket bucket = redisson.getBucket("foo", new StringCodec());
        System.out.println(bucket.get());
        redisson.shutdown();
    }
}

package com.zxm.redis.redisson.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Created by zxm on 8/25/16.
 */
public class ClientCreator {

    private static RedissonClient client = null;

    private static Config createConfig() {
        Config config = new Config();
//        config.setCodec(new MsgPackJacksonCodec());
//        config.useSentinelServers().setMasterName("mymaster").addSentinelAddress("127.0.0.1:26379", "127.0.0.1:26389");
        config.useClusterServers().addNodeAddress("node1:7001", "node1:7002", "node1:7003");
        config.setUseLinuxNativeEpoll(false);
//        config.useSingleServer().setAddress(redisAddress);
//        .setPassword("mypass1");
//        config.useMasterSlaveConnection()
//        .setMasterAddress("127.0.0.1:6379")
//        .addSlaveAddress("127.0.0.1:6399")
//        .addSlaveAddress("127.0.0.1:6389");
        return config;
    }

    public synchronized static RedissonClient createInstance() {
        if(client == null) {
            Config config = createConfig();
            client = Redisson.create(config);
        }
        return client;
    }

}

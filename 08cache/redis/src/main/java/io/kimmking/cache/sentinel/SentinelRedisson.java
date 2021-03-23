package io.kimmking.cache.sentinel;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Redission sentinel配置
 * Date: 2021-03-14
 *
 * @author chenxi
 */
public class SentinelRedisson {
    private static RedissonClient redissonClient = createRedissonClient();

    private static RedissonClient createRedissonClient() {
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName("mymaster")
                .addSentinelAddress("redis://127.0.0.1:26379", "redis://127.0.0.1:26380");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    public static RedissonClient getRedis() {
        return redissonClient;
    }

    public static void close() {
        redissonClient.shutdown();
    }
}

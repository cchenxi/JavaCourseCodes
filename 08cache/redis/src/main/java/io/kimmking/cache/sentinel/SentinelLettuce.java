package io.kimmking.cache.sentinel;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * Lettuce sentinel配置
 * Date: 2021-03-14
 *
 * @author chenxi
 */
public class SentinelLettuce {
    private static RedisClient redisClient = createRedisClient();
    private static StatefulRedisConnection connection;

    private static RedisClient createRedisClient() {
        RedisURI redisURI = RedisURI.builder()
                .withSentinelMasterId("mymaster")
                .withSentinel("127.0.0.1", 26379)
                .withSentinel("127.0.0.1", 26380)
                .build();
        return RedisClient.create(redisURI);
    }

    public static RedisCommands getRedis() {
        connection = redisClient.connect();
        return connection.sync();
    }

    public static void close() {
        connection.close();
        redisClient.shutdown();
    }
}

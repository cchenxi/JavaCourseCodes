package io.kimmking.cache;

import com.alibaba.fastjson.JSON;
import io.kimmking.cache.cluster.ClusterJedis;
import io.kimmking.cache.sentinel.SentinelJedis;
import io.kimmking.cache.sentinel.SentinelLettuce;
import io.kimmking.cache.sentinel.SentinelRedisson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "io.kimmking.cache")
public class RedisApplication implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
//        stringRedisTemplate.opsForValue().set("uptimeSpringDataRedis", new Long(System.currentTimeMillis()).toString());
//        String uptime = stringRedisTemplate.opsForValue().get("uptimeSpringDataRedis");
//        System.out.println(uptime);
    }

    public static void main(String[] args) {

        // C1.最简单demo
//		Jedis jedis = new Jedis("localhost", 6379);
//		System.out.println(jedis.info());
//		jedis.set("uptime", new Long(System.currentTimeMillis()).toString());
//		System.out.println(jedis.get("uptime"));

        // C2.基于sentinel和连接池的demo
//        Jedis sjedis = SentinelJedis.getJedis();
//        System.out.println(sjedis.info());
//        sjedis.set("uptime2", new Long(System.currentTimeMillis()).toString());
//        System.out.println(sjedis.get("uptime2"));
//        SentinelJedis.close();

        // C3. 直接连接sentinel进行操作
//        Jedis jedisSentinel = new Jedis("localhost", 26380); // 连接到sentinel
//        List<Map<String, String>> masters = jedisSentinel.sentinelMasters();
//        System.out.println(JSON.toJSONString(masters));
//        List<Map<String, String>> slaves = jedisSentinel.sentinelSlaves("mymaster");
//        System.out.println(JSON.toJSONString(slaves));


        // 作业：
        // 1. 参考C2，实现基于Lettuce和Redission的Sentinel配置

        // Lettuce demo
//        RedisCommands redisCommands = SentinelLettuce.getRedis();
//        System.out.println(redisCommands.info());
//        redisCommands.set("uptimeLettuce", new Long(System.currentTimeMillis()).toString());
//        System.out.println(redisCommands.get("uptimeLettuce"));
//        SentinelLettuce.close();

        // Redission demo
//        RedissonClient redissonClient = SentinelRedisson.getRedis();
//        RBucket<String> bucket = redissonClient.getBucket("obj");
//        bucket.set("chenxi");
//        String name = bucket.get();
//        System.out.println(name);
//        SentinelRedisson.close();

        // 2. 实现springboot/spring data redis的sentinel配置
        SpringApplication.run(RedisApplication.class, args);

        // 3. 使用jedis命令，使用java代码手动切换 redis 主从
        // 	  Jedis jedis1 = new Jedis("localhost", 6379);
        //    jedis1.info...
        //    jedis1.set xxx...
        //	  Jedis jedis2 = new Jedis("localhost", 6380);
        //    jedis2.slaveof...
        //    jedis2.get xxx

        // 4. 使用C3的方式，使用java代码手动操作sentinel


        // C4. Redis Cluster
        // 作业：
        // 5.使用命令行配置Redis cluster:
        //  1) 以cluster方式启动redis-server
        //  2) 用meet，添加cluster节点，确认集群节点数目
        //  3) 分配槽位，确认分配成功
        //  4) 测试简单的get/set是否成功
        // 然后运行如下代码
 		JedisCluster cluster = ClusterJedis.getJedisCluster();
		for (int i = 0; i < 100; i++) {
			cluster.set("cluster:" + i, "data:" + i);
		}
		System.out.println(cluster.get("cluster:92"));
		ClusterJedis.close();
    }
}

package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Created by 01367594 on 2017/11/1.
 */
@Configuration
//@EnableCaching
public class RedisConfig {
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private long maxWaitMillis;
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//        //设置缓存过期时间
//        //rcm.setDefaultExpiration(60);//秒
//        return rcm;
//    }

//    @Bean("defaultRedisTemplate")
//    public RedisTemplate createRedisTemplate(RedisConnectionFactory factory)
//    {
//        return new RedisTemplate(factory);
//    }

//    @Bean
//    public JedisCluster JedisClusterFactory() {
//        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//        jedisClusterNodes.add(new HostAndPort(host, port));
//        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes, jedisPoolConfig);
//        return jedisCluster;
//    }

    @Bean(name="stringRedisTemplate")
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}

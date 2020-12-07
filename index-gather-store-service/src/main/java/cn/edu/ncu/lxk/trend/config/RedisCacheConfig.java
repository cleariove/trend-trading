package cn.edu.ncu.lxk.trend.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

//这个类主要用于解决乱码的问题，也包含了一些默认配置，没有的话也没关系
@Configuration
//这个注解表示使用spring提供的对redis的默认配置，包括连接数等等
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheConfig
{

    private Duration timeToLive = Duration.ZERO;

    public void setTimeToLive(Duration timeToLive)
    {
        this.timeToLive = timeToLive;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory)
    {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        //解决缓存查询转换异常
        //这部分的作用似乎和Index类上面加Serializable作用一样，实现序列化存储
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //配置序列化，解决乱码问题,配置后redis中查看不会出现乱码
        // 在前端页面不管有没有这段代码都不会乱码
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager manager = RedisCacheManager.builder(factory)
                .cacheDefaults(conf)
                .build();
        return manager;

    }
}

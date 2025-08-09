package demo.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RefreshScope
@Slf4j
public class RedisConfig {
/*    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;*/


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        printRedisConfig(redisConnectionFactory);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }


    private void printRedisConfig(RedisConnectionFactory redisConnectionFactory) {
        // Print Redis connection details
        if (redisConnectionFactory instanceof LettuceConnectionFactory lettuceFactory) {
            String host = lettuceFactory.getHostName();
            int port = lettuceFactory.getPort();
            log.info("Redis Connection - host: {}, port: {}", host, port);
//            log.info("Redis Connection - configured host: {}, port: {}", redisHost, redisPort);
        } else {
            log.info("Using RedisConnectionFactory of type: {}", redisConnectionFactory.getClass().getName());
        }
    }

}

package com.fourback.redisstudy.global.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Getter
@Configuration
@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties("spring.data.redis")
public class RedisConfig {
    private String host;
    private String port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(Integer.parseInt(port));

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public DefaultRedisScript<Void> addOneAndStoreScript() {
        String luaScript = "local itemKey = KEYS[1]\n" +
                "local itemViewKey = KEYS[2]\n" +
                "local viewKey = KEYS[3]\n" +
                "local itemId = ARGV[1]\n" +
                "local userId = ARGV[2]\n" +
                "local inserted = redis.call('PFADD', viewKey, userId)\n" +
                "if inserted == 1 then\n" +
                "   redis.call('HINCRBY', itemKey, 'views', 1)\n" +
                "   redis.call('ZINCRBY', itemViewKey, 1, itemId)\n" +
                "end";
        return new DefaultRedisScript<>(luaScript, Void.class);
    }
}

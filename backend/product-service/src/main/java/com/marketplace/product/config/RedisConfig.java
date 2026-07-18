package com.marketplace.product.config;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.marketplace.product.dto.ProductResponse;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
    ObjectMapper objectMapper = JsonMapper.builder().build();

    JavaType productListType = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, ProductResponse.class);

    JacksonJsonRedisSerializer<List<ProductResponse>> productListSerializer = new JacksonJsonRedisSerializer<>(
        objectMapper, productListType);

    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(1))
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()));

    RedisCacheConfiguration productsCacheConfig = defaultConfig.serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(productListSerializer));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(
            Map.of("products", productsCacheConfig))
        .build();
  }
}

package com.example.chatserver.common.configs.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean // 연결 기본 객체
    @Qualifier("chatPubSub") // @Qualifier("db1") Qualifier : 같은 타입 bean이 여러 개 있을 때 어느 것을 주입할지 지정하는 식별자
    public RedisConnectionFactory chatPubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        // configuration.setDatabase(0); Redis pub sub 기능은 특정 데이터베이스에 의존적이지 않는다.

        return new LettuceConnectionFactory(configuration);
    }

    /* @Qualifier("chatPubSub") : 연결 객체가 여러 개일 경우 의미가 있는 어노테이션, 현재 상황에서는 큰 의미가 없다.
     "chatPubSub"라는 RedisConnectionFactory를 사용하는 StringRedisTemplate Bean 만들기
     일반적으로 StringRedisTemplate가 아니라 RedisTemplate<키 데이터 타입, 값 데이터 타입>을 쓴다. (캐싱 상황)
     StringRedisTemplate : String 형태로 publish
     */
    @Bean // publish 객체
    @Qualifier("chatPubSub")
    public StringRedisTemplate redisTemplate(@Qualifier("chatPubSub") RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean // subscribe 객체
    public RedisMessageListenerContainer container(@Qualifier("chatPubSub") RedisConnectionFactory factory,
                                                   MessageListenerAdapter messageListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic("chat")); // topic : chat에 대해서만 pub, sub
        return container;
    }

    @Bean //redis 에서 수신된 메시지를 처리하는 객체 생성
    public MessageListenerAdapter messageListenerAdapter(RedisPubSubService service) {
        // RedisPubSubService의 특정 메서드가 수신된 메시지를 처리할 수 있도록 지정
        // RedisPubSubService 속 onMessage 메서드가 동작
        return new MessageListenerAdapter(service, "onMessage");

    }

}

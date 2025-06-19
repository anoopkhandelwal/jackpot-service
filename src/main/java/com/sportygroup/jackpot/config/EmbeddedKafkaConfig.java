package com.sportygroup.jackpot.config;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("local")
public class EmbeddedKafkaConfig {

    @Bean
    public EmbeddedKafkaBroker embeddedKafkaBroker() throws Exception {
        Class<?> brokerImplClass = Class.forName("org.springframework.kafka.test.EmbeddedKafkaBroker$EmbeddedKafkaBrokerImpl");

        Constructor<?> constructor = brokerImplClass.getDeclaredConstructor(int.class, boolean.class, String[].class);
        ReflectionUtils.makeAccessible(constructor);
        EmbeddedKafkaBroker broker = (EmbeddedKafkaBroker) constructor.newInstance(1, true, new String[]{"jackpot-bets"});
        Map<String,String> properties = new HashMap();
        properties.put("listeners","PLAINTEXT://0.0.0.0:9092");
        properties.put("advertised.listeners", "PLAINTEXT://localhost:9092");

        broker.kafkaPorts(9092)
                .brokerProperties(properties);

        broker.afterPropertiesSet();
        return broker;
    }

    @Bean
    public DisposableBean brokerCleanup(EmbeddedKafkaBroker broker) {
        return broker::destroy;
    }
}

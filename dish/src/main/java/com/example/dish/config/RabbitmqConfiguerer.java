package com.example.dish.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfiguerer {
    static final String queueName = "spring-boot";
    static final String exchangeName = "spring-boot-exchange";
    static final String routeKey = "queue1";
    @Bean(name=queueName)
    Queue queue() {
        return new Queue(queueName,true);
    }
    @Bean(name=exchangeName)
    DirectExchange exchange() {
        return new DirectExchange(exchangeName,true,false);
    }
    @Bean
    Binding binding(@Qualifier(queueName) Queue queue,
                    @Qualifier(exchangeName) DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }
}

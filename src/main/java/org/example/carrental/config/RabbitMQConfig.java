package org.example.carrental.config;

import org.springframework.amqp.core.*;
import org.example.carrental.dto.PaymentRequest;
import org.example.carrental.dto.PaymentResponse;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_REQUEST_QUEUE = "banque.payment.request";
    public static final String PAYMENT_RESPONSE_QUEUE = "banque.payment.response";
    public static final String EXCHANGE = "car-rental.exchange";

    @Bean
    public Queue paymentRequestQueue() {
        return new Queue(PAYMENT_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue paymentResponseQueue() {
        return new Queue(PAYMENT_RESPONSE_QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding requestBinding(Queue paymentRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentRequestQueue).to(exchange).with(PAYMENT_REQUEST_QUEUE);
    }

    @Bean
    public Binding responseBinding(Queue paymentResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentResponseQueue).to(exchange).with(PAYMENT_RESPONSE_QUEUE);
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        classMapper.setIdClassMapping(Map.of(
                "PaymentRequest", PaymentRequest.class,
                "PaymentResponse", PaymentResponse.class
        ));
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(DefaultClassMapper classMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper);
        return converter;
    }
}

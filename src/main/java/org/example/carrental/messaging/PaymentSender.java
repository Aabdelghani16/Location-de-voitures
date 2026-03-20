package org.example.carrental.messaging;

import org.example.carrental.config.RabbitMQConfig;
import org.example.carrental.dto.PaymentRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentSender {

    private final RabbitTemplate rabbitTemplate;

    public PaymentSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentRequest(PaymentRequest request) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.PAYMENT_REQUEST_QUEUE,
                request
        );
    }
}

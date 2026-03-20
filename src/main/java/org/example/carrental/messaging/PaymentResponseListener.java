package org.example.carrental.messaging;

import org.example.carrental.config.RabbitMQConfig;
import org.example.carrental.dto.PaymentResponse;
import org.example.carrental.model.Purchase;
import org.example.carrental.model.Rental;
import org.example.carrental.repository.PurchaseRepository;
import org.example.carrental.repository.RentalRepository;
import org.example.carrental.service.CarService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseListener {

    private final RentalRepository rentalRepository;
    private final PurchaseRepository purchaseRepository;
    private final CarService carService;

    public PaymentResponseListener(RentalRepository rentalRepository,
                                   PurchaseRepository purchaseRepository,
                                   CarService carService) {
        this.rentalRepository = rentalRepository;
        this.purchaseRepository = purchaseRepository;
        this.carService = carService;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_RESPONSE_QUEUE)
    public void handlePaymentResponse(PaymentResponse response) {
        if ("RENTAL".equals(response.getOrderType())) {
            Rental rental = rentalRepository.findById(response.getOrderId()).orElse(null);
            if (rental != null) {
                if (response.isSuccess()) {
                    rental.setStatus("CONFIRMED");
                    rental.getCar().setAvailable(false);
                    carService.saveCar(rental.getCar());
                } else {
                    rental.setStatus("CANCELLED");
                }
                rentalRepository.save(rental);
            }
        } else if ("PURCHASE".equals(response.getOrderType())) {
            Purchase purchase = purchaseRepository.findById(response.getOrderId()).orElse(null);
            if (purchase != null) {
                if (response.isSuccess()) {
                    purchase.setStatus("CONFIRMED");
                    purchase.getCar().setAvailable(false);
                    carService.saveCar(purchase.getCar());
                } else {
                    purchase.setStatus("CANCELLED");
                }
                purchaseRepository.save(purchase);
            }
        }
    }
}

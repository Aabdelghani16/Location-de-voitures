package org.example.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest implements Serializable {
    private Long orderId;
    private String orderType; // RENTAL or PURCHASE
    private String numeroCompte;
    private double montant;

    public PaymentRequest(Long orderId, String orderType, String numeroCompte, double montant) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.numeroCompte = numeroCompte;
        this.montant = montant;
    }
}

package org.example.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse implements Serializable {
    private Long orderId;
    private String orderType; // RENTAL or PURCHASE
    private boolean success;
    private String message;
}

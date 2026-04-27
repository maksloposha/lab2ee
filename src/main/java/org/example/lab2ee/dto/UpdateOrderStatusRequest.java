package org.example.lab2ee.dto;

import org.example.lab2ee.validation.ValidOrderStatus;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for PATCH /api/orders/{id}/status
 * Uses the custom @ValidOrderStatus annotation.
 */
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Статус є обов'язковим")
    @ValidOrderStatus(message = "Невідомий статус. Допустимі: PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

package com.emeraldia.backend.payload.request;

import com.emeraldia.backend.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateOrderStatusRequest {
  @NotNull(message = "Order status cannot be null")
  private OrderStatus newStatus;

  // Constructor
  public UpdateOrderStatusRequest(OrderStatus newStatus) {
    this.newStatus = newStatus;
  }

  public @NotNull(message = "Order status cannot be null") OrderStatus getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(@NotNull(message = "Order status cannot be null") OrderStatus newStatus) {
    this.newStatus = newStatus;
  }
}

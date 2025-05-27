package com.emeraldia.backend.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
  @NotNull(message = "Product ID cannot be null")
  private String productId; // O Long si el ID de Product es Long

  @NotNull(message = "Quantity cannot be null")
  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer quantity;

  public @NotNull(message = "Product ID cannot be null") String getProductId() {
    return productId;
  }

  public void setProductId(@NotNull(message = "Product ID cannot be null") String productId) {
    this.productId = productId;
  }

  public @NotNull(message = "Quantity cannot be null") @Min(value = 1, message = "Quantity must be at least 1") Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@NotNull(message = "Quantity cannot be null") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity) {
    this.quantity = quantity;
  }
}

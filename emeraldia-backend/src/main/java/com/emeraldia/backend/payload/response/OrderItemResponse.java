package com.emeraldia.backend.payload.response;

import com.emeraldia.backend.dto.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemResponse {
  private String productId;
  private String productName;
  private Integer quantity;
  private BigDecimal priceAtPurchase; // Precio unitario en el momento de la compra
  private BigDecimal subtotal; // Cantidad * precioAtPurchase

  public OrderItemResponse(String productId, String productName, Integer quantity, BigDecimal priceAtPurchase, BigDecimal subtotal) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.priceAtPurchase = priceAtPurchase;
    this.subtotal = subtotal;
  }
}

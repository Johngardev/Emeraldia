package com.emeraldia.backend.payload.response;

import com.emeraldia.backend.dto.OrderItem;
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

  // Constructor para mapear de OrderItem a OrderItemResponse
  public OrderItemResponse(OrderItem orderItem) {
    this.productId = orderItem.getProductId();
    this.productName = orderItem.getProductName();
    this.quantity = orderItem.getQuantity();
    this.priceAtPurchase = orderItem.getPriceAtPurchase();
    // Calcular el subtotal
    if (orderItem.getPriceAtPurchase() != null && orderItem.getQuantity() != null) {
      this.subtotal = orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity()));
    } else {
      this.subtotal = BigDecimal.ZERO; // Manejar el caso de datos nulos
    }
  }
}

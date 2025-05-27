package com.emeraldia.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEmbedded {
  private String productId; // Guardamos solo el ID del producto
  private Integer quantity;

  public BigDecimal getSubtotal(BigDecimal productPrice) {
    if (productPrice != null && quantity != null) {
      return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
    return BigDecimal.ZERO;
  }
}

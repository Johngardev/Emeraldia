package com.emeraldia.backend.dto;

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

  public CartItemEmbedded(String productId, Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}

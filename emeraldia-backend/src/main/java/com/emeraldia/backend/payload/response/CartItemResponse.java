package com.emeraldia.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
  private String id;
  private String productId;
  private String productName;
  private BigDecimal productPrice;
  private Integer quantity;
  private BigDecimal subtotal;
}

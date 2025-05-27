package com.emeraldia.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
  private String id; // ID del carrito de MongoDB
  private String userId; // ID del usuario de MongoDB
  private List<CartItemResponse> items;
  private BigDecimal totalAmount;
}

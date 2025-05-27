package com.emeraldia.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CartResponse {
  private String id; // ID del carrito de MongoDB
  private String userId; // ID del usuario de MongoDB
  private List<CartItemResponse> items;
  private BigDecimal totalAmount;

  public CartResponse(String id, String userId, List<CartItemResponse> items, BigDecimal totalAmount) {
    this.id = id;
    this.userId = userId;
    this.items = items;
    this.totalAmount = totalAmount;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<CartItemResponse> getItems() {
    return items;
  }

  public void setItems(List<CartItemResponse> items) {
    this.items = items;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
}

package com.emeraldia.backend.payload.response;

import com.emeraldia.backend.model.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {
  private String id;
  private String userId; // Solo el ID del usuario, no el objeto completo
  private List<OrderItemResponse> items; // Usaremos OrderItemResponse para los ítems
  private LocalDateTime orderDate;
  private OrderStatus status;
  private BigDecimal totalAmount;
  private String shippingAddress;
  private String billingAddress;

  public OrderResponse(String id, String userId, List<OrderItemResponse> items, LocalDateTime orderDate, OrderStatus status, BigDecimal totalAmount, String shippingAddress, String billingAddress) {
    this.id = id;
    this.userId = userId;
    this.items = items;
    this.orderDate = orderDate;
    this.status = status;
    this.totalAmount = totalAmount;
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
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

  public List<OrderItemResponse> getItems() {
    return items;
  }

  public void setItems(List<OrderItemResponse> items) {
    this.items = items;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(String billingAddress) {
    this.billingAddress = billingAddress;
  }
}

package com.emeraldia.backend.dto;

import java.math.BigDecimal;

public class OrderItem {
  private String productId; // ID del producto en el momento de la compra
  private String productName; // Nombre del producto en el momento de la compra
  private Integer quantity; // Cantidad del producto comprado
  private BigDecimal priceAtPurchase; // Precio unitario del producto en el momento de la compra (CRÍTICO)

  public OrderItem() {
  }

  public OrderItem(String productId, String productName, Integer quantity, BigDecimal priceAtPurchase) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.priceAtPurchase = priceAtPurchase;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPriceAtPurchase() {
    return priceAtPurchase;
  }

  public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
    this.priceAtPurchase = priceAtPurchase;
  }
}

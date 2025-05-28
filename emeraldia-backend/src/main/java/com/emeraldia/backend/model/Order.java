package com.emeraldia.backend.model;

import com.emeraldia.backend.dto.OrderItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "orders")
public class Order {
  @Id
  private String id;

  @DBRef
  private User user; // Referencia al usuario que realizó el pedido

  private List<OrderItem> items = new ArrayList<>(); // Lista de ítems en el pedido

  private LocalDateTime orderDate; // Fecha y hora en que se realizó el pedido
  private OrderStatus status; // Estado actual del pedido (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
  private BigDecimal totalAmount; // Monto total del pedido

  // Información de envío y facturación (puedes crear clases separadas si se vuelven complejas)
  private String shippingAddress;
  private String billingAddress; // Si es diferente de la dirección de envío

  // Constructor
  public Order() {
    this.orderDate = LocalDateTime.now(); // Por defecto, la fecha actual
    this.status = OrderStatus.PENDING; // Por defecto, estado pendiente
    this.totalAmount = BigDecimal.ZERO; // Inicializar en cero
  }

  public Order(User user, List<OrderItem> items, BigDecimal totalAmount, String shippingAddress, String billingAddress) {
    this(); // Llama al constructor por defecto para inicializar fecha y estado
    this.user = user;
    this.items = items;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public void setItems(List<OrderItem> items) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

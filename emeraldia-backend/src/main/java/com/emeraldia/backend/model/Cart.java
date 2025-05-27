package com.emeraldia.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
  @Id
  private String id;

  @DBRef
  private com.emeraldia.backend.model.User user;

  private List<CartItemEmbedded> cartItems = new ArrayList<>(); // Inicializa para evitar NullPointerException

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Constructor para cuando se crea un carrito para un nuevo usuario
  public Cart(User user) {
    this.user = user;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // Métodos utilitarios para manejar ítems (opcional, el servicio también los manejará)
  public Optional<CartItemEmbedded> findCartItemByProductId(String productId) {
    return this.cartItems.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();
  }

  public void addOrUpdateCartItem(CartItemEmbedded newOrUpdatedItem) {
    Optional<CartItemEmbedded> existingItem = findCartItemByProductId(newOrUpdatedItem.getProductId());
    if (existingItem.isPresent()) {
      existingItem.get().setQuantity(newOrUpdatedItem.getQuantity());
    } else {
      this.cartItems.add(newOrUpdatedItem);
    }
  }

  public void removeCartItem(String productId) {
    this.cartItems.removeIf(item -> item.getProductId().equals(productId));
  }
}

package com.emeraldia.backend.service;

import com.emeraldia.backend.model.Cart;
import com.emeraldia.backend.dto.CartItemEmbedded;
import com.emeraldia.backend.model.Product;
import com.emeraldia.backend.model.User;
import com.emeraldia.backend.payload.request.CartItemRequest;
import com.emeraldia.backend.payload.response.CartItemResponse;
import com.emeraldia.backend.payload.response.CartResponse;
import com.emeraldia.backend.repository.CartRepository;
import com.emeraldia.backend.repository.ProductRepository;
import com.emeraldia.backend.repository.UserRepository;
import com.emeraldia.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private UserRepository userRepository;

  //Método auxiliar para obtener el usuario autenticado
  private User getCurrentAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new RuntimeException("User not authenticated.");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return userRepository.findById(userDetails.getId()) // Asegúrate de que UserDetailsImpl.getId() devuelve String
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDetails.getId()));
  }

  // Obtener el carrito del usuario actual (o crear uno si no existe)
  @Transactional
  public CartResponse getOrCreateCartForCurrentUser() {
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser) // Asumiendo que DBRef carga el User
            .orElseGet(() -> {
              Cart newCart = new Cart(currentUser);
              return cartRepository.save(newCart);
            });
    return mapCartToCartResponse(cart);
  }

  // Añadir producto al carrito
  @Transactional
  public CartResponse addProductToCart(CartItemRequest request) {
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser)
            .orElseThrow(() -> new RuntimeException("Cart not found for user. This shouldn't happen."));

    Product product = productRepository.findById(request.getProductId()) // IDs de producto son String
            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

    // Verificar stock
    if (product.getStockQuantity() < request.getQuantity()) {
      throw new IllegalArgumentException("Not enough stock for product: " + product.getName() + ". Available: " + product.getStockQuantity());
    }

    // Buscar si el producto ya está en el carrito
    Optional<CartItemEmbedded> existingCartItem = cart.getCartItems().stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst();

    if (existingCartItem.isPresent()) {
      CartItemEmbedded cartItem = existingCartItem.get();
      int newQuantity = cartItem.getQuantity() + request.getQuantity();
      if (product.getStockQuantity() < newQuantity) {
        throw new IllegalArgumentException("Not enough stock to add " + request.getQuantity() + " more units of product: " + product.getName() + ". Total desired: " + newQuantity + ", Available: " + product.getStockQuantity());
      }
      cartItem.setQuantity(newQuantity);
    } else {
      // Si no existe, crea un nuevo ítem embebido
      CartItemEmbedded newCartItem = new CartItemEmbedded(request.getProductId(), request.getQuantity());
      cart.getCartItems().add(newCartItem);
    }

    cart.setUpdatedAt(LocalDateTime.now()); // Actualiza la fecha de modificación
    cartRepository.save(cart); // Guarda el carrito actualizado

    return mapCartToCartResponse(cart);
  }

  // Actualizar la cantidad de un producto en el carrito
  @Transactional
  public CartResponse updateProductQuantity(String productId, Integer quantity) { // ID de producto es String
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser)
            .orElseThrow(() -> new RuntimeException("Cart not found for user."));

    // Obtener el producto para verificar stock
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

    Optional<CartItemEmbedded> existingCartItem = cart.getCartItems().stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();

    if (existingCartItem.isEmpty()) {
      throw new IllegalArgumentException("Product " + productId + " not found in cart.");
    }

    CartItemEmbedded cartItem = existingCartItem.get();

    if (quantity <= 0) {
      // Si la cantidad es 0 o menos, eliminar el ítem del carrito
      cart.getCartItems().remove(cartItem);
    } else {
      // Verificar stock para la nueva cantidad
      if (product.getStockQuantity() < quantity) {
        throw new IllegalArgumentException("Not enough stock for product: " + product.getName() + ". Available: " + product.getStockQuantity());
      }
      cartItem.setQuantity(quantity);
    }
    cart.setUpdatedAt(LocalDateTime.now()); // Actualiza la fecha de modificación
    cartRepository.save(cart); // Guarda el carrito actualizado

    return mapCartToCartResponse(cart);
  }

  // Eliminar un producto del carrito
  @Transactional
  public CartResponse removeProductFromCart(String productId) { // ID de producto es String
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser)
            .orElseThrow(() -> new RuntimeException("Cart not found for user."));

    boolean removed = cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
    if (!removed) {
      throw new IllegalArgumentException("Product " + productId + " not found in cart to remove.");
    }

    cart.setUpdatedAt(LocalDateTime.now()); // Actualiza la fecha de modificación
    cartRepository.save(cart); // Guarda el carrito actualizado

    return mapCartToCartResponse(cart);
  }

  // Vaciar completamente el carrito
  @Transactional
  public CartResponse clearCart() {
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser)
            .orElseThrow(() -> new RuntimeException("Cart not found for user."));

    cart.getCartItems().clear(); // Limpia la lista de ítems
    cart.setUpdatedAt(LocalDateTime.now()); // Actualiza la fecha de modificación
    cartRepository.save(cart); // Guarda el carrito actualizado

    return mapCartToCartResponse(cart);
  }


  // --- Métodos de mapeo (auxiliares) ---
  private CartResponse mapCartToCartResponse(Cart cart) {
    List<CartItemResponse> itemResponses = cart.getCartItems().stream()
            .map(item -> {
              // Cargar los detalles del producto para el DTO de respuesta
              Product product = productRepository.findById(item.getProductId())
                      .orElse(null); // O lanzar una excepción si el producto no existe

              if (product == null) {
                // Manejar el caso de un producto no encontrado (ej. log, o saltar el ítem)
                return null; // O un CartItemResponse con info limitada
              }

              return new CartItemResponse(item, product);
            })
            .filter(java.util.Objects::nonNull) // Filtrar ítems nulos si el producto no se encontró
            .collect(Collectors.toList());

    BigDecimal totalAmount = itemResponses.stream()
            .map(CartItemResponse::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartResponse(cart.getId(), cart.getUser().getId(), itemResponses, totalAmount);
  }
}
package com.emeraldia.backend.service;

import com.emeraldia.backend.dto.CartItemEmbedded;
import com.emeraldia.backend.dto.OrderItem;
import com.emeraldia.backend.model.*;
import com.emeraldia.backend.payload.response.OrderItemResponse;
import com.emeraldia.backend.payload.response.OrderResponse;
import com.emeraldia.backend.repository.CartRepository;
import com.emeraldia.backend.repository.OrderRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private UserRepository userRepository;

  // Método auxiliar para obtener el usuario autenticado
  private User getCurrentAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new RuntimeException("User not authenticated.");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDetails.getId()));
  }

  /**
   * Crea un nuevo pedido a partir del carrito del usuario actual.
   * Reduce el stock de los productos y vacía el carrito.
   *
   * @param shippingAddress La dirección de envío del pedido.
   * @param billingAddress La dirección de facturación del pedido (opcional, si es diferente).
   * @return OrderResponse del pedido creado.
   */
  @Transactional // ¡CRÍTICO! Esto asegura que la operación es atómica (o todo o nada)
  public OrderResponse createOrderFromCart(String shippingAddress, String billingAddress) {
    User currentUser = getCurrentAuthenticatedUser();
    Cart cart = cartRepository.findByUser(currentUser)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found for current user."));

    if (cart.getCartItems().isEmpty()) {
      throw new IllegalArgumentException("Cannot create an order from an empty cart.");
    }

    List<OrderItem> orderItems = new ArrayList<>();
    BigDecimal totalOrderAmount = BigDecimal.ZERO;

    // Validar stock y construir OrderItems
    for (CartItemEmbedded cartItem : cart.getCartItems()) {
      Product product = productRepository.findById(cartItem.getProductId())
              .orElseThrow(() -> new IllegalArgumentException("Product not found: " + cartItem.getProductId()));

      // Verificar y reducir stock
      if (product.getStockQuantity() < cartItem.getQuantity()) {
        throw new IllegalArgumentException("Insufficient stock for product " + product.getName() + ". Available: " + product.getStockQuantity() + ", Requested: " + cartItem.getQuantity());
      }

      // Crear OrderItem con el precio actual del producto (priceAtPurchase)
      OrderItem orderItem = new OrderItem(
              product.getId(),
              product.getName(),
              cartItem.getQuantity(),
              product.getPrice() // Guardar el precio actual del producto en el momento de la compra
      );
      orderItems.add(orderItem);

      // Calcular el subtotal del OrderItem y añadirlo al total del pedido
      BigDecimal itemSubtotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
      totalOrderAmount = totalOrderAmount.add(itemSubtotal);

      // Reducir stock del producto
      product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
      productRepository.save(product); // Guardar el producto con el stock actualizado
    }

    // Crear el nuevo pedido
    Order newOrder = new Order(
            currentUser,
            orderItems,
            totalOrderAmount,
            shippingAddress,
            billingAddress != null ? billingAddress : shippingAddress // Si billingAddress es null, usa shippingAddress
    );

    Order savedOrder = orderRepository.save(newOrder);

    // Vaciar el carrito después de crear el pedido exitosamente
    cart.getCartItems().clear();
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);

    return mapOrderToOrderResponse(savedOrder);
  }

  /**
   * Obtiene un pedido por su ID para el usuario autenticado.
   *
   * @param orderId El ID del pedido.
   * @return OrderResponse del pedido.
   */
  public OrderResponse getOrderByIdForCurrentUser(String orderId) {
    User currentUser = getCurrentAuthenticatedUser();
    Order order = orderRepository.findByIdAndUser(orderId, currentUser)
            .orElseThrow(() -> new IllegalArgumentException("Order not found or not accessible by current user."));
    return mapOrderToOrderResponse(order);
  }

  /**
   * Obtiene todos los pedidos del usuario autenticado.
   *
   * @return Lista de OrderResponse.
   */
  public List<OrderResponse> getAllOrdersForCurrentUser() {
    User currentUser = getCurrentAuthenticatedUser();
    List<Order> orders = orderRepository.findByUser(currentUser);
    return orders.stream()
            .map(this::mapOrderToOrderResponse)
            .collect(Collectors.toList());
  }

  /**
   * Actualiza el estado de un pedido (normalmente para administradores).
   * Requiere que el llamador tenga los permisos adecuados.
   *
   * @param orderId El ID del pedido a actualizar.
   * @param newStatus El nuevo estado del pedido.
   * @return OrderResponse del pedido actualizado.
   */
  @Transactional
  // Agrega @PreAuthorize para control de acceso si usas Spring Security Method Security
  // @PreAuthorize("hasRole('ADMIN')")
  public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

    // Puedes añadir lógica aquí para transiciones de estado válidas
    // Ej: No se puede cambiar de DELIVERED a PENDING
    if (order.getStatus() == OrderStatus.DELIVERED && newStatus == OrderStatus.PENDING) {
      throw new IllegalArgumentException("Cannot revert delivered order to pending.");
    }
    // Lógica para restaurar stock si se cancela un pedido, etc.
    if (newStatus == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.CANCELLED) {
      // Lógica para restaurar el stock si el pedido se cancela
      for (OrderItem item : order.getItems()) {
        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product != null) {
          product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
          productRepository.save(product);
        }
      }
    }


    order.setStatus(newStatus);
    Order updatedOrder = orderRepository.save(order);
    return mapOrderToOrderResponse(updatedOrder);
  }

  /**
   * Método auxiliar para mapear la entidad Order a OrderResponse DTO.
   */
  private OrderResponse mapOrderToOrderResponse(Order order) {
    List<OrderItemResponse> itemResponses = order.getItems().stream()
            .map(orderItem -> {
              // Creamos el OrderItemResponse usando el constructor generado por @AllArgsConstructor
              BigDecimal calculatedSubtotal = BigDecimal.ZERO;
              if (orderItem.getPriceAtPurchase() != null && orderItem.getQuantity() != null) {
                calculatedSubtotal = orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity()));
              }
              return new OrderItemResponse(
                      orderItem.getProductId(),
                      orderItem.getProductName(),
                      orderItem.getQuantity(),
                      orderItem.getPriceAtPurchase(),
                      calculatedSubtotal // Pasa el subtotal calculado
              );
            })
            .collect(Collectors.toList());

    return new OrderResponse(
            order.getId(),
            order.getUser().getId(),
            itemResponses,
            order.getOrderDate(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getShippingAddress(),
            order.getBillingAddress()
    );
  }
}

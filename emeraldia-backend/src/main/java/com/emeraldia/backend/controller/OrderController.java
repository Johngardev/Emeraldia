package com.emeraldia.backend.controller;

import com.emeraldia.backend.payload.request.CreateOrderRequest;
import com.emeraldia.backend.payload.request.UpdateOrderStatusRequest;
import com.emeraldia.backend.payload.response.OrderResponse;
import com.emeraldia.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OrderController {

  @Autowired
  private OrderService orderService;

  /**
   * Crea un nuevo pedido a partir del carrito del usuario autenticado.
   * Requiere autenticación.
   *
   * @param request Datos de la dirección de envío y facturación.
   * @return ResponseEntity con los detalles del pedido creado.
   */
  @PostMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
    OrderResponse orderResponse = orderService.createOrderFromCart(request.getShippingAddress(), request.getBillingAddress());
    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
  }

  /**
   * Obtiene un pedido específico por su ID para el usuario autenticado.
   * Requiere autenticación.
   *
   * @param orderId El ID del pedido.
   * @return ResponseEntity con los detalles del pedido.
   */
  @GetMapping("/{orderId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
    OrderResponse orderResponse = orderService.getOrderByIdForCurrentUser(orderId);
    return ResponseEntity.ok(orderResponse);
  }

  /**
   * Obtiene todos los pedidos del usuario autenticado.
   * Requiere autenticación.
   *
   * @return ResponseEntity con la lista de pedidos.
   */
  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<List<OrderResponse>> getAllOrdersForCurrentUser() {
    List<OrderResponse> orders = orderService.getAllOrdersForCurrentUser();
    return ResponseEntity.ok(orders);
  }

  /**
   * Actualiza el estado de un pedido. Solo accesible para administradores.
   * Requiere autenticación y rol ADMIN.
   *
   * @param orderId El ID del pedido a actualizar.
   * @param request El nuevo estado del pedido.
   * @return ResponseEntity con los detalles del pedido actualizado.
   */
  @PutMapping("/{orderId}/status")
  @PreAuthorize("hasRole('ADMIN')") // Solo administradores pueden cambiar el estado
  public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId, @Valid @RequestBody UpdateOrderStatusRequest request) {
    OrderResponse orderResponse = orderService.updateOrderStatus(orderId, request.getNewStatus());
    return ResponseEntity.ok(orderResponse);
  }
}

package com.emeraldia.backend.controller;

import com.emeraldia.backend.payload.request.CartItemRequest;
import com.emeraldia.backend.payload.response.CartResponse;
import com.emeraldia.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CartController {

  @Autowired
  private CartService cartService;

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<CartResponse> getCart() {
    CartResponse cart = cartService.getOrCreateCartForCurrentUser();
    return ResponseEntity.ok(cart);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<CartResponse> addProductToCart(@Valid @RequestBody CartItemRequest request) {
    CartResponse updatedCart = cartService.addProductToCart(request);
    return ResponseEntity.ok(updatedCart);
  }

  @PutMapping("/update/{productId}") // productId ahora es String
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<CartResponse> updateProductQuantity(
          @PathVariable String productId, // Cambiado a String
          @RequestParam @Min(value = 0, message = "Quantity must be at least 0") Integer quantity) {
    CartResponse updatedCart = cartService.updateProductQuantity(productId, quantity);
    return ResponseEntity.ok(updatedCart);
  }

  @DeleteMapping("/remove/{productId}") // productId ahora es String
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable String productId) { // Cambiado a String
    CartResponse updatedCart = cartService.removeProductFromCart(productId);
    return ResponseEntity.ok(updatedCart);
  }

  @DeleteMapping("/clear")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<CartResponse> clearCart() {
    CartResponse clearedCart = cartService.clearCart();
    return ResponseEntity.ok(clearedCart);
  }
}

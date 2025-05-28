package com.emeraldia.backend.payload.response;

import com.emeraldia.backend.dto.CartItemEmbedded;
import com.emeraldia.backend.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemResponse {
  private String productId;
  private String productName;
  private BigDecimal productPrice;
  private Integer quantity;
  private BigDecimal subtotal;

  public CartItemResponse(CartItemEmbedded item, Product product) {
    this.productId = item.getProductId();
    this.quantity = item.getQuantity(); // Asigna la cantidad del CartItemEmbedded

    // Asegúrate de que el 'product' y 'product.getPrice()' no sean null
    if (product != null) {
      this.productName = product.getName(); // Asigna el nombre del producto
      this.productPrice = product.getPrice(); // Asigna el precio por unidad del producto

      // Realiza el cálculo del subtotal aquí
      // Asegúrate de que productPrice y quantity no sean null para el cálculo
      if (this.productPrice != null && this.quantity != null) {
        this.subtotal = this.productPrice.multiply(new BigDecimal(this.quantity));
      } else {
        // En caso de que productPrice o quantity sean nulos (lo cual no debería pasar si la DB es limpia)
        this.subtotal = BigDecimal.ZERO;
        // Considera loggear una advertencia aquí si esto ocurre, ya que indica datos incompletos
        System.err.println("Warning: Product price or quantity is null for product " + productId + ". Subtotal set to ZERO.");
      }
    } else {
      // Manejar el caso donde el producto es null (aunque CartService ya lo filtra, es una buena defensa)
      this.productName = "Unknown Product";
      this.productPrice = BigDecimal.ZERO;
      this.subtotal = BigDecimal.ZERO;
      System.err.println("Error: Product is null for CartItem with ID: " + productId + ". Subtotal set to ZERO.");
    }
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

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }
}

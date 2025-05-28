package com.emeraldia.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderRequest {
  @NotBlank(message = "Shipping address cannot be blank")
  private String shippingAddress;

  private String billingAddress; // Opcional, si es el mismo que shippingAddress

  // Constructor (si no usas Lombok AllArgsConstructor)
  public CreateOrderRequest(String shippingAddress, String billingAddress) {
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
  }

  public @NotBlank(message = "Shipping address cannot be blank") String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(@NotBlank(message = "Shipping address cannot be blank") String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(String billingAddress) {
    this.billingAddress = billingAddress;
  }
}

package com.emeraldia.backend.model;

public enum OrderStatus {
  PENDING,        // Pedido creado, a la espera de confirmación de pago
  PROCESSING,     // Pago confirmado, el pedido está siendo preparado
  SHIPPED,        // Pedido enviado
  DELIVERED,      // Pedido entregado
  CANCELLED,      // Pedido cancelado
  REFUNDED        // Pedido reembolsado (opcional, si manejas reembolsos)
}

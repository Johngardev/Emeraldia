package com.emeraldia.backend.repository;

import com.emeraldia.backend.model.Order;
import com.emeraldia.backend.model.OrderStatus;
import com.emeraldia.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
  // Encontrar todos los pedidos de un usuario específico
  List<Order> findByUser(User user);

  // Encontrar un pedido por su ID y el usuario (para seguridad)
  Optional<Order> findByIdAndUser(String orderId, User user);

  // Puedes añadir más métodos de búsqueda si los necesitas,
  // por ejemplo, por estado, por fecha, etc.
  List<Order> findByStatus(OrderStatus status);
}
package com.emeraldia.backend.repository;

import com.emeraldia.backend.model.Cart;
import com.emeraldia.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
  Optional<Cart> findByUser(User user); // Buscar por la referencia al objeto User
  Optional<Cart> findByUserId(String userId);
}

package com.emeraldia.backend.repository;

import com.emeraldia.backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
  List<Product> findByProductType(String productType); // Para filtrar por tipo (lotes, esmeraldas individuales)
  List<Product> findByOrigin(String origin);
  List<Product> findByCaratWeightBetween(BigDecimal minWeight, BigDecimal maxWeight); // Para gemas individuales
  List<Product> findByTotalCaratWeightBetween(BigDecimal minWeight, BigDecimal maxWeight); // Para lotes
  List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
  List<Product> findByNameContainingIgnoreCase(String name);
  List<Product> findByGemType(String gemType); // Para filtrar por tipo de gema (emerald, ruby, etc.)
}

package com.emeraldia.backend.repository;

import com.emeraldia.backend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
  Optional<Category> findByName(String name); // Buscar una categoría por su nombre
  List<Category> findByIsActive(Boolean isActive); // Obtener categorías activas/inactivas
  List<Category> findByType(String type); // Obtener categorías por su tipo (ORIGIN, CUT, etc.)
  List<Category> findByParentCategory(String parentCategoryId); // Obtener subcategorías de una categoría padre
}

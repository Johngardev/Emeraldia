package com.emeraldia.backend.controller;

import com.emeraldia.backend.model.Category;
import com.emeraldia.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * GET /api/categories
   * Obtiene todas las categorías.
   * @return ResponseEntity con una lista de categorías y estado OK.
   */
  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories();
    if (categories.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content si no hay categorías
    }
    return new ResponseEntity<>(categories, HttpStatus.OK); // 200 OK
  }

  /**
   * GET /api/categories/{id}
   * Obtiene una categoría por su ID.
   * @param id El ID de la categoría.
   * @return ResponseEntity con la categoría encontrada y estado OK, o NOT_FOUND.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
    return categoryService.getCategoryById(id)
            .map(category -> new ResponseEntity<>(category, HttpStatus.OK)) // 200 OK
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
  }

  /**
   * POST /api/categories
   * Crea una nueva categoría.
   * @param category La categoría a crear (viene del cuerpo de la petición).
   * @return ResponseEntity con la categoría creada y estado CREATED, o BAD_REQUEST si el nombre ya existe.
   */
  @PostMapping
  public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
    try {
      Category createdCategory = categoryService.createCategory(category);
      return new ResponseEntity<>(createdCategory, HttpStatus.CREATED); // 201 Created
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
      // En un sistema real, querrías devolver un DTO de error con el mensaje de 'e.getMessage()'
    }
  }

  /**
   * PUT /api/categories/{id}
   * Actualiza una categoría existente.
   * @param id El ID de la categoría a actualizar.
   * @param category Los datos actualizados de la categoría (viene del cuerpo de la petición).
   * @return ResponseEntity con la categoría actualizada y estado OK, o NOT_FOUND si no existe, o BAD_REQUEST.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable String id, @Valid @RequestBody Category category) {
    try {
      Category updatedCategory = categoryService.updateCategory(id, category);
      return new ResponseEntity<>(updatedCategory, HttpStatus.OK); // 200 OK
    } catch (RuntimeException e) { // Captura RuntimeException para Not Found
      return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
      // En un sistema real, podrías tener una excepción personalizada para un manejo más granular
    }
  }

  /**
   * DELETE /api/categories/{id}
   * Elimina una categoría por su ID.
   * @param id El ID de la categoría a eliminar.
   * @return ResponseEntity con estado NO_CONTENT.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
    categoryService.deleteCategory(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
  }

  /**
   * GET /api/categories/status/{isActive}
   * Obtiene categorías por su estado de actividad (true/false).
   * @param isActive Booleano para filtrar categorías activas o inactivas.
   * @return ResponseEntity con una lista de categorías y estado OK.
   */
  @GetMapping("/status/{isActive}")
  public ResponseEntity<List<Category>> getCategoriesByStatus(@PathVariable Boolean isActive) {
    List<Category> categories = categoryService.getCategoriesByStatus(isActive);
    if (categories.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  /**
   * GET /api/categories/type/{type}
   * Obtiene categorías por su tipo (ej. "ORIGIN", "CUT", "GEM_TYPE").
   * @param type El tipo de categoría.
   * @return ResponseEntity con una lista de categorías y estado OK.
   */
  @GetMapping("/type/{type}")
  public ResponseEntity<List<Category>> getCategoriesByType(@PathVariable String type) {
    List<Category> categories = categoryService.getCategoriesByType(type.toUpperCase()); // Asumiendo que el tipo se guarda en mayúsculas
    if (categories.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  /**
   * GET /api/categories/parent/{parentCategoryId}
   * Obtiene subcategorías de una categoría padre.
   * @param parentCategoryId El ID de la categoría padre.
   * @return ResponseEntity con una lista de subcategorías y estado OK.
   */
  @GetMapping("/parent/{parentCategoryId}")
  public ResponseEntity<List<Category>> getSubcategories(@PathVariable String parentCategoryId) {
    List<Category> subcategories = categoryService.getSubcategories(parentCategoryId);
    if (subcategories.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(subcategories, HttpStatus.OK);
  }
}
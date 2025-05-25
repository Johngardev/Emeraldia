package com.emeraldia.backend.service;

import com.emeraldia.backend.model.Category;
import com.emeraldia.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * Obtiene todas las categorías.
   * @return Una lista de todas las categorías.
   */
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  /**
   * Obtiene una categoría por su ID.
   * @param id El ID de la categoría.
   * @return Un Optional que contiene la categoría si se encuentra, o vacío si no.
   */
  public Optional<Category> getCategoryById(String id) {
    return categoryRepository.findById(id);
  }

  /**
   * Crea una nueva categoría.
   * Asigna automáticamente las fechas de creación y actualización.
   * @param category La categoría a crear.
   * @return La categoría creada con su ID asignado.
   * @throws IllegalArgumentException Si el nombre de la categoría ya existe.
   */
  public Category createCategory(Category category) {
    // Validación: Asegurarse de que el nombre de la categoría sea único
    if (categoryRepository.findByName(category.getName()).isPresent()) {
      throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists.");
    }
    // Puedes generar el slug aquí si no viene del frontend
    if (category.getSlug() == null || category.getSlug().isEmpty()) {
      category.setSlug(generateSlug(category.getName()));
    }
    return categoryRepository.save(category);
  }

  /**
   * Actualiza una categoría existente.
   * Asigna automáticamente la fecha de actualización.
   * @param id El ID de la categoría a actualizar.
   * @param updatedCategory Los datos de la categoría actualizada.
   * @return La categoría actualizada.
   * @throws RuntimeException Si la categoría no se encuentra.
   * @throws IllegalArgumentException Si el nuevo nombre de la categoría ya existe en otra categoría.
   */
  public Category updateCategory(String id, Category updatedCategory) {
    return categoryRepository.findById(id)
            .map(existingCategory -> {
              // Validación de nombre único para actualizaciones
              Optional<Category> categoryWithSameName = categoryRepository.findByName(updatedCategory.getName());
              if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(id)) {
                throw new IllegalArgumentException("Category with name '" + updatedCategory.getName() + "' already exists for another category.");
              }

              existingCategory.setName(updatedCategory.getName());
              existingCategory.setDescription(updatedCategory.getDescription());
              existingCategory.setParentCategory(updatedCategory.getParentCategory());
              existingCategory.setOrder(updatedCategory.getOrder());
              existingCategory.setImageUrl(updatedCategory.getImageUrl());
              existingCategory.setIsActive(updatedCategory.getIsActive());
              existingCategory.setType(updatedCategory.getType()); // Actualiza el tipo de categoría
              // Vuelve a generar slug si el nombre cambia y no viene en updatedCategory
              if (!existingCategory.getName().equals(updatedCategory.getName()) && (updatedCategory.getSlug() == null || updatedCategory.getSlug().isEmpty())) {
                existingCategory.setSlug(generateSlug(updatedCategory.getName()));
              } else if (updatedCategory.getSlug() != null && !updatedCategory.getSlug().isEmpty()) {
                existingCategory.setSlug(updatedCategory.getSlug());
              }

              return categoryRepository.save(existingCategory);
            })
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
  }

  /**
   * Elimina una categoría por su ID.
   * @param id El ID de la categoría a eliminar.
   */
  public void deleteCategory(String id) {
    categoryRepository.deleteById(id);
  }

  /**
   * Obtiene categorías por su estado de actividad.
   * @param isActive true para activas, false para inactivas.
   * @return Una lista de categorías.
   */
  public List<Category> getCategoriesByStatus(Boolean isActive) {
    return categoryRepository.findByIsActive(isActive);
  }

  /**
   * Obtiene categorías por su tipo (ej. "ORIGIN", "CUT").
   * @param type El tipo de categoría.
   * @return Una lista de categorías.
   */
  public List<Category> getCategoriesByType(String type) {
    return categoryRepository.findByType(type);
  }

  /**
   * Obtiene subcategorías de una categoría padre.
   * @param parentCategoryId El ID de la categoría padre.
   * @return Una lista de subcategorías.
   */
  public List<Category> getSubcategories(String parentCategoryId) {
    return categoryRepository.findByParentCategory(parentCategoryId);
  }

  private String generateSlug(String text) {
    return text.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "") // Eliminar caracteres no alfanuméricos excepto espacios y guiones
            .replaceAll("\\s+", "-") // Reemplazar espacios con guiones
            .replaceAll("^-|-$", ""); // Eliminar guiones al principio y al final
  }
}

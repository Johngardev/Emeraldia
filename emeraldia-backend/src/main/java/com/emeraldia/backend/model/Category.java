package com.emeraldia.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@Document(collection = "categories")
public class Category {
  @Id
  private String id;
  @NotBlank(message = "Category name cannot be empty")
  @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
  private String name;
  private String slug; // Para URLs amigables (ej. /productos?categoria=esmeraldas-colombianas)
  @Size(max = 200, message = "Category description cannot exceed 200 characters")
  private String description;
  private String parentCategory; // ID de la categoría padre para subcategorías
  private Integer order; // Para definir el orden de visualización
  private String imageUrl; // Icono o imagen para la categoría
  @NotNull(message = "Active status cannot be null")
  private Boolean isActive;
  @Field("category_type")
  private String type; // Define el tipo de categoría: "ORIGIN", "CUT", "COLOR", "GEM_TYPE", "COLLECTION", etc.

  public Category() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(String parentCategory) {
    this.parentCategory = parentCategory;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

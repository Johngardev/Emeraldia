package com.emeraldia.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "products")
public class Product {
  @Id
  private String id;
  @NotBlank(message = "Product name cannot be empty")
  @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
  private String name;
  @Size(max = 500, message = "Description cannot exceed 500 characters")
  private String description;
  @NotNull(message = "Price cannot be null")
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  private BigDecimal price;
  @NotNull(message = "Stock quantity cannot be null")
  @Min(value = 0, message = "Stock quantity cannot be negative")
  private Integer stockQuantity;
  @NotEmpty(message = "At least one image URL is required")
  private List<String> imageUrls;

  // --- Atributos específicos para diferentes tipos de productos ---
  @Field("product_type")
  private String productType; // e.g., "SINGLE_EMERALD", "GEMSTONE_LOT", "OTHER_GEMSTONE"

  // Atributos comunes a la mayoría de las gemas (pueden ser nulos para lotes o si no aplica)
  private String origin;
  @DecimalMin(value = "0.0", inclusive = false, message = "Carat weight must be greater than 0")
  private BigDecimal caratWeight; // Para una sola gema, peso total para un lote
  private String color;
  private String cut;
  private String clarity;
  private String treatment;
  private String certification;
  private String dimensions;

  // Atributos específicos para LOTES
  @Min(value = 1, message = "Number of pieces must be at least 1 for a lot")
  @Field("number_of_pieces")
  private Integer numberOfPieces;
  @DecimalMin(value = "0.0", inclusive = false, message = "Total carat weight must be greater than 0 for a lot")
  @Field("total_carat_weight")
  private BigDecimal totalCaratWeight;
  @Size(max = 500, message = "Lot description cannot exceed 500 characters")
  @Field("lot_description")
  private String lotDescription;
  @Valid
  @Field("gemstones_in_lot")
  private List<GemstoneInLot> gemstonesInLot;

  @NotBlank(message = "Gem type cannot be empty")
  @Field("gem_type")
  private String gemType;

  @NotBlank(message = "Category ID cannot be empty")
  private String categoryId;

  // Un mapa para atributos aún más dinámicos y específicos
  @Field("additional_properties")
  private Map<String, String> additionalProperties;

  public Product() {}

  // Métodos para la lista de gemas individuales en un lote
  @Data
  public static class GemstoneInLot {
    @NotBlank(message = "Gem type in lot cannot be empty")
    private String gemType;
    @NotNull(message = "Carat weight in lot cannot be null")
    @DecimalMin(value = "0.01", message = "Carat weight in lot must be greater than 0")
    private BigDecimal caratWeight;
    private String color;
    private String cut;

    public GemstoneInLot() {}

    public String getGemType() { return gemType; }
    public void setGemType(String gemType) { this.gemType = gemType; }

    public BigDecimal getCaratWeight() { return caratWeight; }
    public void setCaratWeight(BigDecimal caratWeight) { this.caratWeight = caratWeight; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCut() { return cut; }
    public void setCut(String cut) { this.cut = cut; }
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal getPrice() {
    return price;
  }

  public void setPrice(@NotNull(message = "Price cannot be null") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price) {
    this.price = price;
  }

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public List<String> getImageUrls() {
    return imageUrls;
  }

  public void setImageUrls(List<String> imageUrls) {
    this.imageUrls = imageUrls;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public BigDecimal getCaratWeight() {
    return caratWeight;
  }

  public void setCaratWeight(BigDecimal caratWeight) {
    this.caratWeight = caratWeight;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getCut() {
    return cut;
  }

  public void setCut(String cut) {
    this.cut = cut;
  }

  public String getClarity() {
    return clarity;
  }

  public void setClarity(String clarity) {
    this.clarity = clarity;
  }

  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  public String getCertification() {
    return certification;
  }

  public void setCertification(String certification) {
    this.certification = certification;
  }

  public String getDimensions() {
    return dimensions;
  }

  public void setDimensions(String dimensions) {
    this.dimensions = dimensions;
  }

  public Integer getNumberOfPieces() {
    return numberOfPieces;
  }

  public void setNumberOfPieces(Integer numberOfPieces) {
    this.numberOfPieces = numberOfPieces;
  }

  public BigDecimal getTotalCaratWeight() {
    return totalCaratWeight;
  }

  public void setTotalCaratWeight(BigDecimal totalCaratWeight) {
    this.totalCaratWeight = totalCaratWeight;
  }

  public String getLotDescription() {
    return lotDescription;
  }

  public void setLotDescription(String lotDescription) {
    this.lotDescription = lotDescription;
  }

  public List<GemstoneInLot> getGemstonesInLot() {
    return gemstonesInLot;
  }

  public void setGemstonesInLot(List<GemstoneInLot> gemstonesInLot) {
    this.gemstonesInLot = gemstonesInLot;
  }

  public String getGemType() {
    return gemType;
  }

  public void setGemType(String gemType) {
    this.gemType = gemType;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public Map<String, String> getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Map<String, String> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }
}

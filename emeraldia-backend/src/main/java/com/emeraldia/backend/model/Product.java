package com.emeraldia.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "products")
public class Product {
  @Id
  private String id;
  private String name;
  private String description;
  private Long price;
  private Integer stockQuantity;
  private List<String> imageUrls;

  // --- Atributos específicos para diferentes tipos de productos ---
  private String productType; // e.g., "SINGLE_EMERALD", "GEMSTONE_LOT", "OTHER_GEMSTONE"

  // Atributos comunes a la mayoría de las gemas (pueden ser nulos para lotes o si no aplica)
  private String origin;
  private BigDecimal caratWeight; // Para una sola gema, peso total para un lote
  private String color;
  private String cut;
  private String clarity;
  private String treatment;
  private String certification;

  // Atributos específicos para LOTES
  private Integer numberOfPieces;
  private BigDecimal totalCaratWeight;
  private String lotDescription;
  private List<GemstoneInLot> gemstonesInLot;

  private String gemType;

  // Un mapa para atributos aún más dinámicos y específicos
  private Map<String, String> additionalProperties;

  // Métodos para la lista de gemas individuales en un lote
  @Data
  public static class GemstoneInLot {
    private String gemType;
    private BigDecimal weight;
    private String color;
    private String cut;
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

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
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

  public Map<String, String> getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Map<String, String> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }
}

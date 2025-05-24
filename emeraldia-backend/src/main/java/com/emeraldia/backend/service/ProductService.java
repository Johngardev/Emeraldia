package com.emeraldia.backend.service;

import com.emeraldia.backend.model.Product;
import com.emeraldia.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Optional<Product> getProductById(String id) {
    return productRepository.findById(id);
  }

  public Product create(Product product){
    if (product.getProductType() == null | product.getProductType().isEmpty()) {
      throw new IllegalArgumentException("Product type cannot be null or empty.");
    }
    return productRepository.save(product);
  }

  public Product update(String id, Product updatedProduct) {
    return productRepository.findById(id)
            .map(product -> {
              // Actualiza solo los campos que vienen en updatedProduct y que deben ser actualizables
              product.setName(updatedProduct.getName());
              product.setDescription(updatedProduct.getDescription());
              product.setPrice(updatedProduct.getPrice());
              product.setStockQuantity(updatedProduct.getStockQuantity());
              product.setImageUrls(updatedProduct.getImageUrls());

              // Actualizar campos específicos si aplican
              product.setProductType(updatedProduct.getProductType()); // Asegúrate de manejar cambios de tipo con cuidado
              product.setOrigin(updatedProduct.getOrigin());
              product.setCaratWeight(updatedProduct.getCaratWeight());
              product.setColor(updatedProduct.getColor());
              product.setCut(updatedProduct.getCut());
              product.setClarity(updatedProduct.getClarity());
              product.setTreatment(updatedProduct.getTreatment());
              product.setCertification(updatedProduct.getCertification());
              product.setNumberOfPieces(updatedProduct.getNumberOfPieces());
              product.setTotalCaratWeight(updatedProduct.getTotalCaratWeight());
              product.setLotDescription(updatedProduct.getLotDescription());
              product.setGemstonesInLot(updatedProduct.getGemstonesInLot());
              product.setGemType(updatedProduct.getGemType());
              product.setAdditionalProperties(updatedProduct.getAdditionalProperties());


              return productRepository.save(product);
            })
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
  }

  public void delete(String id) {
    productRepository.deleteById(id);
  }

  public List<Product> getProductByType(String productType) {
    return productRepository.findByProductType(productType);
  }

  public List<Product> getProductsByGemType(String gemType) {
    return productRepository.findByGemType(gemType);
  }
}
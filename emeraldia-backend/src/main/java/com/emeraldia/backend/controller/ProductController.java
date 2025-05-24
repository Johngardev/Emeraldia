package com.emeraldia.backend.controller;

import com.emeraldia.backend.model.Product;
import com.emeraldia.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable String id) {
    return productService.getProductById(id)
            .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product product) {
    try {
      Product createdProduct = productService.create(product);
      return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
    try {
      Product updatedProduct = productService.update(id, product);
      return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } catch (RuntimeException e) {
      return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("{/id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    productService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/type/{productType}")
  public ResponseEntity<List<Product>> getProductByType(@PathVariable String productType) {
    List<Product> products = productService.getProductByType(productType.toUpperCase());
    if (products.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @GetMapping("/gemType/{gemType}")
  public ResponseEntity<List<Product>> getProductsByGemType(@PathVariable String gemType) {
    List<Product> products = productService.getProductsByGemType(gemType);
    if (products.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(products, HttpStatus.OK);
  }
}

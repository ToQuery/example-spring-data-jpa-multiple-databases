package io.github.toquery.example.spring.data.jpa.multiple.databases.product.dao;

import io.github.toquery.example.spring.data.jpa.multiple.databases.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */

public interface ProductRepository extends JpaRepository<Product, Integer> { }

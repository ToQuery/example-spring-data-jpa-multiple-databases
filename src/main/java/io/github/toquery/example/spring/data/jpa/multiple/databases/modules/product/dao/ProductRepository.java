package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.dao;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */

public interface ProductRepository extends JpaRepository<Product, Integer> {
}

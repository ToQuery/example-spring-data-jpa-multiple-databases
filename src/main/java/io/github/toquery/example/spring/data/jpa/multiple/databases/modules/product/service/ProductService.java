package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.service;

import io.github.toquery.example.spring.data.jpa.multiple.databases.ExampleSpringDataJpaMultipleDatabasesApplication;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.dao.ProductRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
@RequiredArgsConstructor
@Transactional(transactionManager = "productTransactionManager")
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product init() {
        Product product = new Product();
        product.setId(1);
        product.setName("test");
        product.setPrice(12.3d);
        product.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        productRepository.saveAndFlush(product);
        return product;
    }

    public Product save() {
        Product product = new Product();
        product.setName("test");
        product.setPrice(12.3d);
        product.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return this.save(product);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void delete() {
        productRepository.deleteAll();
    }
    public void delete(Product product) {
        productRepository.delete(product);
    }


    public List<Product> list() {
        return productRepository.findAll();
    }

    public List<Product> update() {
        List<Product> products = this.list();
        products.forEach(product -> product.setUpdateDate(LocalDateTime.now()));
        return productRepository.saveAllAndFlush(products);
    }
}

package io.github.toquery.example.spring.data.jpa.multiple.databases;

import io.github.toquery.example.spring.data.jpa.multiple.databases.product.dao.ProductRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.product.model.Product;
import io.github.toquery.example.spring.data.jpa.multiple.databases.user.dao.UserRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExampleSpringDataJpaMultipleDatabasesApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional("userTransactionManager")
    public void whenCreatingUser_thenCreated() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@test.com");
        user.setAge(20);
        user = userRepository.save(user);

        assertNotNull(userRepository.findById(user.getId()).get());
    }

    @Test
    @Transactional("userTransactionManager")
    public void whenCreatingUsersWithSameEmail_thenRollback() {
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john@test.com");
        user1.setAge(20);
        user1 = userRepository.save(user1);
        assertNotNull(userRepository.findById(user1.getId()).get());

        User user2 = new User();
        user2.setId(123456);
        user2.setName("Tom");
        user2.setEmail("john@test.com");
        user2.setAge(10);
        try {
            user2 = userRepository.save(user2);
        } catch (DataIntegrityViolationException e) {
        }

        assertFalse(userRepository.findById(user2.getId()).isPresent());
    }

    @Rollback(value = false)
    @Test
    @Transactional("productTransactionManager")
    public void whenCreatingProduct_thenCreated() {
        Product product = new Product();
        product.setName("Book");
        product.setId(2);
        product.setPrice(20.d);
        product = productRepository.save(product);

        assertNotNull(productRepository.findById(product.getId()).get());
    }

}

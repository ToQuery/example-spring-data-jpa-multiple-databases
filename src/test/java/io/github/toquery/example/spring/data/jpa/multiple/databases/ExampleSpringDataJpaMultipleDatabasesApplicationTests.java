package io.github.toquery.example.spring.data.jpa.multiple.databases;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.dao.OrderRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity.Order;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.dao.PayRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity.Pay;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.dao.ProductRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity.Product;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.dao.UserRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class ExampleSpringDataJpaMultipleDatabasesApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayRepository payRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @SneakyThrows
    @Test
    @Transactional(transactionManager = "userTransactionManager")
    public void whenCreatingUser_thenCreated() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@test.com");
        user.setAge(20);
        user = userRepository.saveAndFlush(user);

        User dbUser = userRepository.findById(user.getId()).get();
        log.info(objectMapper.writeValueAsString(dbUser));
        assertNotNull(dbUser);
    }
    @SneakyThrows
    @Test
    @Transactional(transactionManager = "productTransactionManager")
    public void whenCreatingProduct_thenCreated() {
        Product product = new Product();
        product.setId(2223);
        product.setName("Book");
        product.setPrice(20.d);
        product = productRepository.saveAndFlush(product);

        Product dbProduct = productRepository.findById(product.getId()).get();

        log.info(objectMapper.writeValueAsString(dbProduct));

        assertNotNull(dbProduct);
    }

    @SneakyThrows
    @Test
    @Transactional(transactionManager = "orderTransactionManager")
    public void whenCreatingOrder_thenCreated() {
        Order order = new Order();
        order.setOrderNo("Book");
        order.setCreatedDate(LocalDateTime.now());
        order = orderRepository.saveAndFlush(order);

        Order dbOrder = orderRepository.findById(order.getId()).get();
        log.info(objectMapper.writeValueAsString(dbOrder));

        assertNotNull(dbOrder);
    }

    @SneakyThrows
    @Test
    @Transactional(transactionManager = "payTransactionManager")
    public void whenCreatingPay_thenCreated() {
        Pay pay = new Pay();
        pay.setId(2);
        pay.setPayNo("Book");
        pay.setPayDate(LocalDateTime.now());
        pay = payRepository.saveAndFlush(pay);

        Pay dbPay = payRepository.findById(pay.getId()).get();
        log.info(objectMapper.writeValueAsString(dbPay));

        assertNotNull(dbPay);
    }

    @Test
    @Transactional(transactionManager = "userTransactionManager")
    public void whenCreatingUsersWithSameEmail_thenRollback() {
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john@test.com");
        user1.setAge(20);
        user1 = userRepository.saveAndFlush(user1);
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



}

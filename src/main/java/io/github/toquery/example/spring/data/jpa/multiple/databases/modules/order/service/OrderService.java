package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.service;

import io.github.toquery.example.spring.data.jpa.multiple.databases.ExampleSpringDataJpaMultipleDatabasesApplication;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.dao.OrderRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
@RequiredArgsConstructor
@Data
@Transactional(transactionManager = "orderTransactionManager", rollbackFor = Exception.class)
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public Order init() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNo("1L");
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return orderRepository.saveAndFlush(order);
    }

    public Order save() {
        Order order = new Order();
        order.setOrderNo("1L");
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return this.save(order);
    }

    public Order save(Order order) {
        return orderRepository.saveAndFlush(order);
    }
    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void delete() {
        orderRepository.deleteAll();
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }


    public List<Order> list() {
        return orderRepository.findAll();
    }

    public List<Order> update() {
        List<Order> orders = this.list();
        orders.forEach(order -> order.setUpdateDate(LocalDateTime.now()));
        return orderRepository.saveAllAndFlush(orders);
    }

    public List<Order> rollback(Boolean rollback) {
        this.delete();
        if (rollback) {
            throw new RuntimeException("Rollback Order");
        }
        return this.list();
    }
}

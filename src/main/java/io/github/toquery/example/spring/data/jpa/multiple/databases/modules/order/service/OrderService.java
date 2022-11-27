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
@Transactional(transactionManager = "orderTransactionManager")
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

    public Order save(Order order) {
        return orderRepository.save(order);
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

    public Order update() {
        Order order = orderRepository.getReferenceById(1);
        order.setUpdateDate(LocalDateTime.now());
        return orderRepository.saveAndFlush(order);
    }
}

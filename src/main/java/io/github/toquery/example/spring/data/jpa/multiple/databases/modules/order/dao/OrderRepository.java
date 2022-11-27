package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.dao;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */

public interface OrderRepository extends JpaRepository<Order, Integer> {
}

package io.github.toquery.example.spring.data.jpa.multiple.databases.bff;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity.Order;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity.Pay;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity.Product;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.entity.User;

/**
 * 结果
 */
public record ResultDTO(User user, Order order, Product product, Pay pay) {
}

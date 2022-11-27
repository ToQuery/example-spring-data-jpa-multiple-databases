package io.github.toquery.example.spring.data.jpa.multiple.databases.bff;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity.Order;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity.Pay;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity.Product;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.entity.User;

import java.util.List;

/**
 * 结果
 */
public record ResultListDTO(List<User> users, List<Order> orders, List<Product> products, List<Pay> pays) {
}

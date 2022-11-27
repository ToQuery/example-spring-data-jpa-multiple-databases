package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.dao;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */

public interface PayRepository extends JpaRepository<Pay, Integer> {
}

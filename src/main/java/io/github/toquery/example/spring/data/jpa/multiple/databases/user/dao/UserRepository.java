package io.github.toquery.example.spring.data.jpa.multiple.databases.user.dao;

import io.github.toquery.example.spring.data.jpa.multiple.databases.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */

public interface UserRepository extends JpaRepository<User, Integer> {
}

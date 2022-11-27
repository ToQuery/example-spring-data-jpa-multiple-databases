package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.service;

import io.github.toquery.example.spring.data.jpa.multiple.databases.ExampleSpringDataJpaMultipleDatabasesApplication;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.dao.UserRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Transactional(transactionManager = "userTransactionManager")
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User init() {
        User user = new User();
        user.setId(1);
        user.setName("toquery");
        user.setEmail(UUID.randomUUID() + "@toquery.com");
        user.setAge(11);
        user.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return this.userRepository.saveAndFlush(user);
    }

    public User save() {
        User user = new User();
        user.setName("toquery");
        user.setEmail(UUID.randomUUID() + "@toquery.com");
        user.setAge(11);
        user.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return this.save(user);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public List<User> list() {
        return this.userRepository.findAll();
    }


    public User findById(Integer id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public void delete() {
        this.userRepository.deleteAll();
    }
    public void delete(User user) {
        this.userRepository.delete(user);
    }


    public List<User> update() {
        List<User> users = this.list();
        users.forEach(user -> user.setUpdateDate(LocalDateTime.now()));
        return userRepository.saveAllAndFlush(users);
    }
}

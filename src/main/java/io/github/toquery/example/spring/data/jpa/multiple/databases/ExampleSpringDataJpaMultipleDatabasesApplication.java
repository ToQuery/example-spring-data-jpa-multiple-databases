package io.github.toquery.example.spring.data.jpa.multiple.databases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class ExampleSpringDataJpaMultipleDatabasesApplication {

    public static final LocalDateTime INIT_DATE = LocalDateTime.of(2022, 11, 22, 11, 22, 33);

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringDataJpaMultipleDatabasesApplication.class, args);
    }

}

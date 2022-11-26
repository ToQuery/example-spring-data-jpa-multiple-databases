package io.github.toquery.example.spring.data.jpa.multiple.databases;

import io.github.toquery.example.spring.data.jpa.multiple.databases.config.MultipleDatabasesHibernateJpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(MultipleDatabasesHibernateJpaConfiguration.class)
@SpringBootApplication
public class ExampleSpringDataJpaMultipleDatabasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleSpringDataJpaMultipleDatabasesApplication.class, args);
	}

}

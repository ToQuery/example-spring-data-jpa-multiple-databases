package io.github.toquery.example.spring.data.jpa.multiple.databases.core.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
@EnableJpaRepositories(
        basePackages = PersistenceOrderConfiguration.DAO_PACKAGES,
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager"
)
public class PersistenceOrderConfiguration {

    public static final String DAO_PACKAGES = "io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.dao";
    public static final String[] ENTITY_PACKAGES = {"io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.entity"};

    @Autowired
    private JpaProperties properties;

    @Bean
    @ConfigurationProperties(prefix = "spring.multiple-database.order")
    public DataSource orderDataSource() {
        return new HikariDataSource();
    }

    @Bean
    static PersistenceManagedTypes orderPersistenceManagedTypes(ResourceLoader resourceLoader) {
        return new PersistenceManagedTypesScanner(resourceLoader).scan(ENTITY_PACKAGES);
    }

    @Bean
    public EntityManagerFactoryBuilder orderEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                                        ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                                        ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }



    @Bean
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(@Qualifier("orderDataSource") DataSource orderDataSource,
                                                                            @Qualifier("orderPersistenceManagedTypes") PersistenceManagedTypes orderPersistenceManagedTypes,
                                                                            @Qualifier("orderEntityManagerFactoryBuilder") EntityManagerFactoryBuilder orderEntityManagerFactoryBuilder) {
        return orderEntityManagerFactoryBuilder
                .dataSource(orderDataSource)
                .managedTypes(orderPersistenceManagedTypes)
                // .packages(PersistenceOrderConfiguration.ENTITY_PACKAGES)
                .persistenceUnit("orderPersistenceUnit")
                .build();
    }



    @Bean
    public PlatformTransactionManager orderTransactionManager(@Qualifier("orderEntityManagerFactory") LocalContainerEntityManagerFactoryBean orderEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(orderEntityManagerFactory.getObject());
        return transactionManager;
    }
}

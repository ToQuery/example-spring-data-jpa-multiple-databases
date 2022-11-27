package io.github.toquery.example.spring.data.jpa.multiple.databases.core.config;

import com.zaxxer.hikari.HikariDataSource;
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
        basePackages = PersistenceProductConfiguration.DAO_PACKAGES,
        entityManagerFactoryRef = "productEntityManagerFactory",
        transactionManagerRef = "productTransactionManager"
)
public class PersistenceProductConfiguration {

    public static final String DAO_PACKAGES = "io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.dao";
    public static final String[] ENTITY_PACKAGES = {"io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.entity"};

    @Autowired
    private JpaProperties properties;

    @Bean
    @ConfigurationProperties(prefix = "spring.multiple-database.product")
    public DataSource productDataSource() {
        return new HikariDataSource();
    }

    @Bean
    static PersistenceManagedTypes productPersistenceManagedTypes(ResourceLoader resourceLoader) {
        return new PersistenceManagedTypesScanner(resourceLoader).scan(ENTITY_PACKAGES);
    }

    @Bean
    public EntityManagerFactoryBuilder productEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                                          ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                                          ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(@Qualifier("productDataSource") DataSource productDataSource,
                                                                              @Qualifier("productPersistenceManagedTypes") PersistenceManagedTypes productPersistenceManagedTypes,
                                                                              @Qualifier("productEntityManagerFactoryBuilder") EntityManagerFactoryBuilder productEntityManagerFactoryBuilder) {
        return productEntityManagerFactoryBuilder
                .dataSource(productDataSource)
                //.packages(ENTITY_PACKAGES)
                .persistenceUnit("productPersistenceUnit")
                .managedTypes(productPersistenceManagedTypes)
                .build();
    }


    @Bean
    public PlatformTransactionManager productTransactionManager(@Qualifier("productEntityManagerFactory") LocalContainerEntityManagerFactoryBean productEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(productEntityManagerFactory.getObject());
        return transactionManager;
    }
}

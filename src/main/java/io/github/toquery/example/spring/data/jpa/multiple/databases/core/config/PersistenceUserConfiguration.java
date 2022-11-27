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
import org.springframework.context.annotation.Primary;
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
        basePackages = PersistenceUserConfiguration.DAO_PACKAGES,
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
public class PersistenceUserConfiguration {

    public static final String DAO_PACKAGES = "io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.dao";
    public static final String[] ENTITY_PACKAGES = {"io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.entity"};

    @Autowired
    private JpaProperties properties;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.multiple-database.user")
    public DataSource userDataSource() {
        return new HikariDataSource();
    }

    @Bean
    static PersistenceManagedTypes userPersistenceManagedTypes(ResourceLoader resourceLoader) {
        return new PersistenceManagedTypesScanner(resourceLoader).scan(ENTITY_PACKAGES);
    }

    @Bean
    public EntityManagerFactoryBuilder userEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                                       ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                                       ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(@Qualifier("userDataSource") DataSource userDataSource,
                                                                           @Qualifier("userPersistenceManagedTypes") PersistenceManagedTypes userPersistenceManagedTypes,
                                                                           @Qualifier("userEntityManagerFactoryBuilder") EntityManagerFactoryBuilder userEntityManagerFactoryBuilder) {
        return userEntityManagerFactoryBuilder
                .dataSource(userDataSource)
                .managedTypes(userPersistenceManagedTypes)
                //.packages(PersistenceUserConfiguration.ENTITY_PACKAGES)
                .persistenceUnit("userPersistenceUnit")
                .build();
    }



    @Bean
    @Primary
    public PlatformTransactionManager userTransactionManager(LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userEntityManagerFactory.getObject());
        return transactionManager;
    }
}

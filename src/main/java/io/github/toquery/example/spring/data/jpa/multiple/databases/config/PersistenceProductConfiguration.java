package io.github.toquery.example.spring.data.jpa.multiple.databases.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "io.github.toquery.example.spring.data.jpa.multiple.databases.product.dao",
        entityManagerFactoryRef = "productEntityManager",
        transactionManagerRef = "productTransactionManager"
)
public class PersistenceProductConfiguration {
    @Autowired
    private Environment env;

    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private MultipleDatabasesHibernateJpaConfiguration multipleDatabasesHibernateJpaConfiguration;

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = multipleDatabasesHibernateJpaConfiguration.createJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        if (jpaProperties.getDatabase() != null) {
            adapter.setDatabase(jpaProperties.getDatabase());
        }
        if (jpaProperties.getDatabasePlatform() != null) {
            adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        }
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }

    @Bean
    public EntityManagerFactoryBuilder productEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                                   ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                                   ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

    @Bean
    @Primary
    static PersistenceManagedTypes persistenceManagedTypes(ResourceLoader resourceLoader) {
        return new PersistenceManagedTypesScanner(resourceLoader).scan("io.github.toquery.example.spring.data.jpa.multiple.databases.product.model");
    }



    @Bean
    public LocalContainerEntityManagerFactoryBean productEntityManager(DataSource productDataSource,
                                                                       EntityManagerFactoryBuilder productEntityManagerFactoryBuilder,
                                                                       PersistenceManagedTypes persistenceManagedTypes) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(productDataSource());
//        em.setPackagesToScan("io.github.toquery.example.spring.data.jpa.multiple.databases.product.model");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        HashMap<String, Object> properties = new HashMap<>();
////        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
////        properties.put("hibernate.dialect",  "MYSQL");
//        em.setJpaPropertyMap(properties);
//        return em;


        Map<String, Object> vendorProperties = multipleDatabasesHibernateJpaConfiguration.getVendorProperties(productDataSource);
        multipleDatabasesHibernateJpaConfiguration.customizeVendorProperties(vendorProperties);
        return productEntityManagerFactoryBuilder
                .dataSource(productDataSource)
                .managedTypes(persistenceManagedTypes)
                .properties(vendorProperties)
                .packages("io.github.toquery.example.spring.data.jpa.multiple.databases.product.model")
//                .mappingResources(multipleDatabasesHibernateJpaConfiguration.getMappingResources())
//                .jta(multipleDatabasesHibernateJpaConfiguration.isJta())
                .build();

    }

    @Bean
    @ConfigurationProperties(prefix = "spring.multiple-database.product")
    public DataSource productDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager productTransactionManager(LocalContainerEntityManagerFactoryBean productEntityManager) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(productEntityManager.getObject());
        return transactionManager;
    }

}

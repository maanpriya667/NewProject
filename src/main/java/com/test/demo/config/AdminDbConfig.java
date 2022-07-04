package com.test.demo.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "adminEMF", transactionManagerRef = "adminTM",
        basePackages = {"com.test.demo.admin.repository"})
public class AdminDbConfig {

    @Bean(name = "adminDS")
    @ConfigurationProperties(prefix = "admin.datasource")
    public DataSource adminDS() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "adminEMF")
    public LocalContainerEntityManagerFactoryBean adminEMF(
            EntityManagerFactoryBuilder builder, @Qualifier("adminDS") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.test.demo.admin.entity").persistenceUnit("adminDB")
                .build();
    }

    @Bean(name = "adminTM")
    public PlatformTransactionManager transactionManager(
            @Qualifier("adminEMF") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}

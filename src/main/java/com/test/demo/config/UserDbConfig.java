package com.test.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Driver;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "userEMF", transactionManagerRef = "userTM",
        basePackages = {"com.test.demo.repository"})

public class UserDbConfig {

    @Autowired
    private UserDbProperty userDbProperty;

    private DataSource createDS(String url) throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setUrl(url);
        Properties properties = new Properties();
        properties.setProperty("jdbc.driver", userDbProperty.getDriverClassName());

        dataSource.setDriverClass((Class<? extends Driver>)Class.forName(properties.getProperty("jdbc.driver")));
        dataSource.setUsername(userDbProperty.getUsername());
        dataSource.setPassword(userDbProperty.getPassword());

        return dataSource;
    }

    @Bean
    public DataSource routingDS() throws Exception {
        CustomRoutingDS customRoutingDS = new CustomRoutingDS();

        DataSource master = createDS(userDbProperty.getUrl());

        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("master", master);

        userDbProperty.getSlaveList().forEach(slave -> {
            try {
                dataSourceMap.put(slave.getName(), createDS(slave.getUrl()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        customRoutingDS.setTargetDataSources(dataSourceMap);
        customRoutingDS.setDefaultTargetDataSource(master);
        return customRoutingDS;
    }

    @Primary
    @Bean(name = "userDS")
    public DataSource userDS() throws Exception {
        return new LazyConnectionDataSourceProxy(routingDS());
    }

    @Primary
    @Bean(name = "userEMF")
    public LocalContainerEntityManagerFactoryBean userEMF(
            EntityManagerFactoryBuilder builder, @Qualifier("userDS") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.test.demo.entity").persistenceUnit("userDB")
                .build();
    }

    @Primary
    @Bean(name = "userTM")
    public PlatformTransactionManager transactionManager(
            @Qualifier("userEMF") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

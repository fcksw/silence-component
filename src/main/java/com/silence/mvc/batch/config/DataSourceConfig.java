package com.silence.mvc.batch.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.net.MalformedURLException;


//public class DataSourceConfig {


//    @Bean("jobmetaDatasource")
////    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.jobmeta")
//    public DataSource jobmetaDatasource() {
//        return DataSourceBuilder.create().build();
//    }


/**
 * 使用sqlite作为数据源
 */
//    @Value("org/springframework/batch/core/schema-drop-sqlite.sql")
//    private Resource dropReopsitoryTables;
//
//    @Value("org/springframework/batch/core/schema-sqlite.sql")
//    private Resource dataReopsitorySchema;



//
//    private ResourcelessTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }




    // mysql 初始化
//    @Bean
//    public DataSourceInitializer dataSourceInitializer(@Qualifier("jobmetaDatasource") DataSource dataSource) {
//        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//        resourceDatabasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-mysql.sql"));
//        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
//        dataSourceInitializer.setDataSource(dataSource);
//        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
//        return dataSourceInitializer;
//    }



//}

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


@Configuration
public class DataSourceConfig {


//    @Bean("jobmetaDatasource")
////    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.jobmeta")
//    public DataSource jobmetaDatasource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean("sourceDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDatasource() {
        return DataSourceBuilder.create().build();
    }


    @Bean("readSqlSessionFactory")
    @Primary
    public SqlSessionFactory readSqlSessionFactory(@Qualifier("sourceDatasource")DataSource sourceDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(sourceDataSource);
        return sqlSessionFactoryBean.getObject();
    }




    @Bean("targetDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.target")
    public DataSource targetDatasource() {
        return DataSourceBuilder.create().build();
    }


    @Bean("writeSqlSessionFactory")
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier("targetDatasource")DataSource targetDatasource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(targetDatasource);
        return sqlSessionFactoryBean.getObject();
    }

/**
 * 使用sqlite作为数据源
 */
//    @Value("org/springframework/batch/core/schema-drop-sqlite.sql")
//    private Resource dropReopsitoryTables;
//
//    @Value("org/springframework/batch/core/schema-sqlite.sql")
//    private Resource dataReopsitorySchema;


    @Bean("sqliteDataSource")
    @Primary
    public DataSource sqliteDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:repository.sqlite");
        return dataSource;
    }


    /**
     * 初始化sqlite数据库
     *
     * @param sqliteDataSource
     * @return
     * @throws MalformedURLException
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource sqliteDataSource)
            throws MalformedURLException {
        ResourceDatabasePopulator databasePopulator =
                new ResourceDatabasePopulator();

        Resource dropReopsitoryTables = new ClassPathResource("org/springframework/batch/core/schema-drop-sqlite.sql");

        Resource dataReopsitorySchema = new ClassPathResource("org/springframework/batch/core/schema-sqlite.sql");

        databasePopulator.addScript(dropReopsitoryTables);
        databasePopulator.addScript(dataReopsitorySchema);
        databasePopulator.setIgnoreFailedDrops(true);
        databasePopulator.setContinueOnError(true);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(sqliteDataSource);
        initializer.setDatabasePopulator(databasePopulator);

        return initializer;
    }
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



}

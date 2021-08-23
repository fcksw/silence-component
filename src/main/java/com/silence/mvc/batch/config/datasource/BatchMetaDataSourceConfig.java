package com.silence.mvc.batch.config.datasource;

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
public class BatchMetaDataSourceConfig {



    @Primary
    @Bean("sqliteDataSource")
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
}

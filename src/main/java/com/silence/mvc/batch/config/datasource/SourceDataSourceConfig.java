package com.silence.mvc.batch.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.silence.mvc.batch.dao.read"})
public class SourceDataSourceConfig {



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


}


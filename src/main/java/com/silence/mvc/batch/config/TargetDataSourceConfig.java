package com.silence.mvc.batch.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TargetDataSourceConfig {

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

}

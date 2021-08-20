package com.silence.mvc.batch.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = {"com.silence.mvc.batch.dao.write"}, sqlSessionFactoryRef = "writeSqlSessionFactory")
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
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

}

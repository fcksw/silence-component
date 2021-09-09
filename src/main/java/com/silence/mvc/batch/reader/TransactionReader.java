package com.silence.mvc.batch.reader;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionReader {


    @Resource(name = "readSqlSessionFactory")
    private SqlSessionFactory readSqlSessionFactory;



    @Bean("myBatisPagingItemReader")
    @StepScope
    public MyBatisPagingItemReader<Transaction> myBatisPagingItemReader(

            @Value("#{jobParameters['status']}")String status) {

        Map<String, Object> map = new HashMap<>();
        map.put("status", status);


        return new MyBatisPagingItemReaderBuilder<Transaction>()
                .sqlSessionFactory(readSqlSessionFactory)
                .pageSize(1000)
                .queryId("com.silence.mvc.batch.dao.read.TransactionReadDao.selectAllTransaction")
                .parameterValues(map)
                .build();

    }

}

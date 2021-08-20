package com.silence.mvc.batch.reader;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TransactionReader {


    @Resource(name = "readSqlSessionFactory")
    private SqlSessionFactory readSqlSessionFactory;



    @Bean("myBatisPagingItemReader")
    public MyBatisPagingItemReader<Transaction> myBatisPagingItemReader() {
        return new MyBatisPagingItemReaderBuilder<Transaction>()
                .sqlSessionFactory(readSqlSessionFactory)
                .pageSize(1000)
                .queryId("com.silence.mvc.batch.dao.TransactionDao.selectAll")
//                .parameterValues()
                .build();
    }
}

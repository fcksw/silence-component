package com.silence.mvc.batch.reader;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionReader implements ItemReader<Transaction> {


    @Resource(name = "readSqlSessionFactory")
    private SqlSessionFactory readSqlSessionFactory;



    @Override
    public Transaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Map<String, Object> params = new HashMap<>();

        MyBatisPagingItemReader myBatisPagingItemReader = new MyBatisPagingItemReader();
        myBatisPagingItemReader.setSqlSessionFactory(readSqlSessionFactory);

        return null;
    }
}

package com.silence.mvc.batch.writer;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TransactionWriter {

    @Resource(name = "writeSqlSessionFactory")
    private SqlSessionFactory writeSqlSessionFactory;


    @Bean("myBatisBatchItemWriter")
    public MyBatisBatchItemWriter<Transaction> myBatisBatchItemWriter() {
        return new MyBatisBatchItemWriterBuilder<Transaction>()
                .sqlSessionFactory(writeSqlSessionFactory)
                .statementId("com.silence.mvc.batch.dao.write.TransactionWriteDao.insertTransaction")
                .build();
    }


}

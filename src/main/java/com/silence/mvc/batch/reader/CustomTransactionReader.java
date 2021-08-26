package com.silence.mvc.batch.reader;

import com.silence.mvc.batch.dao.read.TransactionReadDao;
import com.silence.mvc.batch.entity.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CustomTransactionReader extends AbstractPagingItemReader<Transaction> {


    @Resource
    private TransactionReadDao transactionReadDao;



    @Override
    protected void doReadPage() {

    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }
}

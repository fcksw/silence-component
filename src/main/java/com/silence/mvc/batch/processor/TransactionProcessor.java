package com.silence.mvc.batch.processor;

import com.silence.mvc.batch.entity.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionProcessor {


    @Bean("transactionFunctionProcessor")
    public FunctionItemProcessor<Transaction, Transaction> transactionFunctionProcessor() {
        return new FunctionItemProcessor<>(transaction -> {
            transaction.setStateMsg("batch job");
            return transaction;
        });
    }


}

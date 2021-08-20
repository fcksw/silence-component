package com.silence.mvc.batch.writer;

import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionReconciliationWriter implements ItemWriter<String> {


    @Override
    public void write(List<? extends String> items) throws Exception {

    }
}

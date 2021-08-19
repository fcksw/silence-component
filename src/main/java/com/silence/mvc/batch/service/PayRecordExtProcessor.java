package com.silence.mvc.batch.service;

import com.silence.mvc.batch.entity.PayRecord;
import com.silence.mvc.batch.entity.PayRecordExt;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PayRecordExtProcessor implements ItemProcessor<PayRecord, PayRecordExt> {

    //此处注入业务处理service

    @Override
    public PayRecordExt process(PayRecord payRecord) throws Exception {
        PayRecordExt payRecordExt = new PayRecordExt();
        payRecordExt.setExt("test");
        return payRecordExt;
    }
}

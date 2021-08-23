package com.silence.mvc.controller;

import com.silence.mvc.batch.dao.read.TransactionReadDao;
import com.silence.mvc.batch.dao.write.TransactionWriteDao;
import com.silence.mvc.batch.entity.Transaction;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/hello")
    public String hello(){
        return "success";
    }


    @Autowired
    private AmqpTemplate rabbitTemplate;


    @GetMapping("/rabbit/push")
    public String pushRabbitMqMsg(String message) {
        rabbitTemplate.convertAndSend("order-direct-exchange", "order.024.confirm.success", message);
        return "OK";
    }

    @Autowired
    JobLauncher jobLauncher;

    @Resource
    private Job splitPayRecordJob;

    @Resource
    private Job migratePayRecordJob;

    @Resource
    private Job transactionJob;

    @Resource
    private TransactionReadDao transactionReadDao;

    @Resource
    private TransactionWriteDao transactionWriteDao;


    @RequestMapping("/batchJob")
    public String handle() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(splitPayRecordJob, jobParameters);

        return "Batch job has been invoked";
    }

    @RequestMapping("/transactionJob")
    public String transactionJob() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(transactionJob, jobParameters);

        return "Batch job has been invoked";
    }


    @RequestMapping("/read")
    public List<Transaction> read() {
        return transactionReadDao.selectAllTransaction();
    }


    @RequestMapping("/write")
    public List<Transaction> write() {
        return transactionWriteDao.selectAll();
    }

}

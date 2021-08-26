package com.silence.mvc.controller;

import com.silence.mvc.batch.dao.read.TransactionReadDao;
import com.silence.mvc.batch.dao.write.TransactionWriteDao;
import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private Job customTransactionJob;

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

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("status", "confirm_failed")
                .toJobParameters();
        jobLauncher.run(transactionJob, jobParameters);

        return "Batch job has been invoked";
    }


    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @RequestMapping("/page")
    public List<Transaction> page() {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("_page", 0L);
        parameters.put("_pagesize", 10L);
        parameters.put("_skiprows", 0L);

        parameters.put("status", "confirm_failed");

        List<Transaction> result = sqlSessionTemplate.selectList("com.silence.mvc.batch.dao.read.TransactionReadDao.selectAllTransactionByStatus", parameters);

        return result;

    }

    @RequestMapping("/customTransactionJob")
    public String customTransactionJob() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("status", "confirm_failed")
                .toJobParameters();
        jobLauncher.run(customTransactionJob, jobParameters);

        return "Batch job has been invoked";
    }


}

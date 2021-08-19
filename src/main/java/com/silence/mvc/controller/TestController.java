package com.silence.mvc.controller;

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


    @RequestMapping("/batchJob")
    public String handle() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(splitPayRecordJob, jobParameters);

        return "Batch job has been invoked";
    }


}

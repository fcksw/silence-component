package com.silence.mvc;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.silence.mvc"}, exclude = { MongoDataAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableBatchProcessing
public class SilenceMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SilenceMvcApplication.class, args);
    }

}

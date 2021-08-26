package com.silence.mvc.batch.config;

import com.silence.mvc.batch.config.datasource.BatchMetaDataSourceConfig;
import com.silence.mvc.batch.config.datasource.SourceDataSourceConfig;
import com.silence.mvc.batch.config.datasource.TargetDataSourceConfig;
import com.silence.mvc.batch.entity.PayRecord;
import com.silence.mvc.batch.entity.PayRecordRowMapper;
import com.silence.mvc.batch.entity.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Configuration
@ComponentScan(basePackageClasses = {SourceDataSourceConfig.class, TargetDataSourceConfig.class, BatchMetaDataSourceConfig.class})
public class SpringBatchConfig {

    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private DataSource sqliteDataSource;

    @Resource
    private DataSource targetDatasource;

    @Resource
    private DataSource sourceDatasource;



    /**
     * 使用内存数据源存储任务执行数据
     * @return
     */
    @Bean
    public BatchConfigurer batchConfigurer() {
        return new DefaultBatchConfigurer(sqliteDataSource);
    }


    @Bean
    public JdbcPagingItemReader<PayRecord> payRecordReader() throws Exception {
        JdbcPagingItemReader<PayRecord> reader = new JdbcPagingItemReader<>();

        final SqlPagingQueryProviderFactoryBean sqlProviderFactory = new SqlPagingQueryProviderFactoryBean();
        sqlProviderFactory.setDataSource(sourceDatasource);
        sqlProviderFactory.setSelectClause("select * ");
        sqlProviderFactory.setFromClause("from pay_record");
//        sqlProviderFactory.setWhereClause("id > " + minId + " and id <= " + maxId);
        sqlProviderFactory.setSortKey("id");
        reader.setQueryProvider(sqlProviderFactory.getObject());
        reader.setDataSource(sourceDatasource);
        reader.setPageSize(1000);
        reader.setRowMapper(new PayRecordRowMapper());
        reader.afterPropertiesSet();
        reader.setSaveState(true);
        return reader;
    }


    @Bean
    public ItemWriter<? extends PayRecord> targetPayRecordWriter() {
        return new JdbcBatchItemWriterBuilder<PayRecord>()
                .dataSource(targetDatasource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(
                        "INSERT INTO `pay_record` (\n"
                                + "`id`,\n"
                                + "`user_id`,\n"
                                + "`pay_detail`,\n"
                                + "`pay_status`,\n"
                                + "`create_time`,\n"
                                + "`update_time`\n"
                                + ")\n"
                                + "VALUES\n"
                                + "\t(\n"
                                + ":id,"
                                + ":userId,"
                                + ":payDetail,"
                                + ":payStatus,"
                                + ":createTime,"
                                + ":updateTime"
                                + ")")
                .build();
    }


    /**
     * 删除器
     */
    @Bean
    public ItemWriter<PayRecord> deletePayRecordWriter() {
        return new JdbcBatchItemWriterBuilder<PayRecord>()
                .dataSource(targetDatasource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("delete from pay_record where id = :id")
                .build();
    }


    @Bean
    public CompositeItemWriter<PayRecord> compositePayRecordItemWriter(@Qualifier("deletePayRecordWriter") ItemWriter deleteWriter, @Qualifier("targetPayRecordWriter") ItemWriter targetPayRecordWriter) {
        CompositeItemWriter<PayRecord> compositeItemWriter = new CompositeItemWriter<>();
        List<ItemWriter<? super PayRecord>> list = new ArrayList<>();
        list.add(deleteWriter);
        list.add(targetPayRecordWriter);
        compositeItemWriter.setDelegates(list);
        return compositeItemWriter;
    }



    @Bean
    public Step migratePayRecordStep(@Qualifier("payRecordReader") JdbcPagingItemReader<PayRecord> payRecordReader, @Qualifier(value = "compositePayRecordItemWriter") CompositeItemWriter compositeItemWriter) {
        return this.stepBuilderFactory.get("migratePayRecordStep")
                .<PayRecord, PayRecord>chunk(1000)
                .reader(payRecordReader)
                .processor(new PassThroughItemProcessor())
                .writer(compositeItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor("migrate_thread"))
                .throttleLimit(10)
                .build();
    }

    /**
     * job1 : 单表操作writer
     * @param step
     * @return
     */
    @Bean
    public Job migratePayRecordJob(@Qualifier("migratePayRecordStep") Step step) {
        return this.jobBuilderFactory.get("migratePayRecordJob")
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    public ItemWriter<? super PayRecord> payRecordOneWriter() {
        return new JdbcBatchItemWriterBuilder<PayRecord>()
                .dataSource(targetDatasource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(
                        "INSERT INTO `pay_record_1` (\n"
                                + "`id`,\n"
                                + "`user_id`,\n"
                                + "`pay_detail`,\n"
                                + "`pay_status`,\n"
                                + "`create_time`,\n"
                                + "`update_time`\n"
                                + ")\n"
                                + "VALUES\n"
                                + "\t(\n"
                                + ":id,"
                                + ":userId,"
                                + ":payDetail,"
                                + ":payStatus,"
                                + ":createTime,"
                                + ":updateTime"
                                + ")")
                .build();
    }

    @Bean
    public ItemWriter<? super PayRecord> payRecordTwoWriter() {
        return new JdbcBatchItemWriterBuilder<PayRecord>()
                .dataSource(targetDatasource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(
                        "INSERT INTO `pay_record_2` (\n"
                                + "`id`,\n"
                                + "`user_id`,\n"
                                + "`pay_detail`,\n"
                                + "`pay_status`,\n"
                                + "`create_time`,\n"
                                + "`update_time`\n"
                                + ")\n"
                                + "VALUES\n"
                                + "\t(\n"
                                + ":id,"
                                + ":userId,"
                                + ":payDetail,"
                                + ":payStatus,"
                                + ":createTime,"
                                + ":updateTime"
                                + ")")
                .build();
    }



    //根据条件拆分为多表
    @Bean
    public ClassifierCompositeItemWriter<? super PayRecord> classifierItemWriter(@Qualifier("payRecordOneWriter") ItemWriter payRecordOneWriter, @Qualifier("payRecordTwoWriter") ItemWriter payRecordTwoWriter){
        ClassifierCompositeItemWriter<PayRecord> classifierCompositeItemWriter = new ClassifierCompositeItemWriter<>();
        classifierCompositeItemWriter.setClassifier(
                (Classifier<PayRecord, ItemWriter<? super PayRecord>>) record -> {
                    ItemWriter<? super PayRecord> itemWriter;
                    if(record.getId() %2 ==0 ){
                        itemWriter = payRecordOneWriter;
                    }else {
                        itemWriter = payRecordTwoWriter;
                    }
                    return itemWriter;
                });
        return classifierCompositeItemWriter;
    }



    @Bean
    public Step splitPayRecordStep(@Qualifier("payRecordReader") JdbcPagingItemReader<PayRecord> payRecordReader, @Qualifier(value = "classifierItemWriter") ClassifierCompositeItemWriter itemWriter) {
        return this.stepBuilderFactory.get("splitPayRecordStep")
                .<PayRecord, PayRecord>chunk(1000)
                .reader(payRecordReader)
                .processor(new PassThroughItemProcessor())
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor("migrate_thread"))
                .throttleLimit(10)
                .build();
    }

    @Bean
    public Job splitPayRecordJob(@Qualifier("splitPayRecordStep") Step step) {
        return this.jobBuilderFactory.get("splitPayRecordJob")
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }


    /**
     * job1 : 数据读取与写入
     *
     *      1、任务运行信息写入内存 sqlite数据库
     *      2、任务数据来源使用 mybatisPageItemReader, 并独立配置 sourceDateSource
     *      3、任务数据写入使用 mybatisItemWriter
     */

    @Bean("transactionJob")
    public Job transactionJob(@Qualifier("transactionStep")Step transactionStep) {
        return this.jobBuilderFactory.get("transactionJob")
                .start(transactionStep)
                .build();
    }



    @Bean("transactionStep")
    public Step transactionStep(@Qualifier("myBatisPagingItemReader")ItemReader<Transaction> myBatisPagingItemReader
                        , @Qualifier("myBatisBatchItemWriter") ItemWriter<Transaction> myBatisBatchItemWriter
                        , @Qualifier("transactionFunctionProcessor") ItemProcessor<Transaction, Transaction> transactionFunctionProcessor){

        return this.stepBuilderFactory.get("transactionStep")
                .<Transaction, Transaction>chunk(1000)
                .reader(myBatisPagingItemReader)
                .processor(transactionFunctionProcessor)
                .writer(myBatisBatchItemWriter)
                .throttleLimit(10).build();

    }



    @Bean("customTransactionStep")
    public Step customTransactionStep(@Qualifier("customTransactionReader")ItemReader<Transaction> customTransactionReader
            , @Qualifier("myBatisBatchItemWriter") ItemWriter<Transaction> myBatisBatchItemWriter
            , @Qualifier("transactionFunctionProcessor") ItemProcessor<Transaction, Transaction> transactionFunctionProcessor){

        return this.stepBuilderFactory.get("transactionStep")
                .<Transaction, Transaction>chunk(1000)
                .reader(customTransactionReader)
                .processor(transactionFunctionProcessor)
                .writer(myBatisBatchItemWriter)
                .throttleLimit(10).build();

    }



    @Bean("customTransactionJob")
    public Job customTransactionJob(@Qualifier("customTransactionStep")Step customTransactionStep) {
        return this.jobBuilderFactory.get("customTransactionJob")
                .start(customTransactionStep)
                .build();
    }



}

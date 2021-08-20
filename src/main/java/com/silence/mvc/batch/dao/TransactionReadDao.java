package com.silence.mvc.batch.dao;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionReadDao {

    List<Transaction> selectAll();

}

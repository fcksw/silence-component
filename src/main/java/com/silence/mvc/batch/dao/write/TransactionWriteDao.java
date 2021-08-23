package com.silence.mvc.batch.dao.write;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionWriteDao {

    boolean insert();

    List<Transaction> selectAll();
}

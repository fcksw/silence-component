package com.silence.mvc.batch.dao.read;

import com.silence.mvc.batch.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionReadDao {

    List<Transaction> selectAllTransaction();

    List<Transaction> selectAllTransactionByStatus(Transaction transaction);

    List<Transaction> selectTransactionLimit10();
}

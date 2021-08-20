package com.silence.mvc.batch.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionWriteDao {

    boolean insert();

}

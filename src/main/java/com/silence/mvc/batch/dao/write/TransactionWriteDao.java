package com.silence.mvc.batch.dao.write;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionWriteDao {

    boolean insert();

}

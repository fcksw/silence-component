package com.silence.mvc.batch.dao;

import com.silence.mvc.batch.entity.PayRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRecordDao {

    List<PayRecord> selectByPage();
}

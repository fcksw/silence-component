package com.silence.mvc.batch.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Transaction implements Serializable {

    private Long id;
    private String transactionId;
    private Long uid;
    private String fdCode;
    private String fdName;
    private Double amount = 0D;
    private Double volume = 0D;
    private String action;
    private String status;
    private Long ts;
    private Long tsConfirm;
    private String orderType;
    private String orderId;
    private Double feeRate = 0D;
    private Double discountRate = 0D;
    private String bankcardNo;
    private String bankName;
    private String bankSerial;
    private String channel;
    private String tradeAccount;
    private Long createdAt;
    private Long updatedAt;
    private long confirmDate;
    private long queryDate;
    private String targetFdCode;
    private String targetFdName;
    private Double targetVolume = 0D;

    private Double confirmAmount = 0D;
    private Double confirmVolume = 0D;
    private String planCode;
    private String errorMsg;
    private String errorCode;
    private String targetPlanCode;
    private String source;
    private String originTid;
    private Double confirmUnitNav;
    private Double targetUnitNav;
    private Double fare;
    private Double targetFare;
    private String cardId;
    private String saleType;
    private Integer stateCode;

    private String stateMsg;

    private String transactionSerialNumber;


}

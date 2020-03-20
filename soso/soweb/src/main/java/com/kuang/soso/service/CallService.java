package com.kuang.soso.service;


import com.kuang.pojo.MobileCard;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;

public interface CallService {
    double call_fund = 0.2;
    int call(int minCount, MobileCard card) throws InsufficientException, ExceedAmountException;
}
